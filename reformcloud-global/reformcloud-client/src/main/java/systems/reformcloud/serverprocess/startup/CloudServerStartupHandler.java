/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.startup;

import lombok.Getter;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.meta.startup.stages.ProcessStartupStage;
import systems.reformcloud.network.packets.out.*;
import systems.reformcloud.serverprocess.screen.ScreenHandler;
import systems.reformcloud.template.TemplatePreparer;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

@Getter
public class CloudServerStartupHandler {
    private Path path;
    private ServerStartupInfo serverStartupInfo;
    private Process process;
    private int port;

    private boolean toShutdown;

    private Template loaded;
    private ScreenHandler screenHandler;

    private ProcessStartupStage processStartupStage;

    private boolean firstGroupStart;

    /**
     * Creates a instance of a CloudServerStartupHandler
     *
     * @param serverStartupInfo
     */
    public CloudServerStartupHandler(final ServerStartupInfo serverStartupInfo, final boolean firstGroupStart) {
        this.processStartupStage = ProcessStartupStage.WAITING;
        this.serverStartupInfo = serverStartupInfo;
        this.firstGroupStart = firstGroupStart;

        if (this.serverStartupInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)) {
            this.path = Paths.get("reformcloud/static/servers/" + serverStartupInfo.getName());
            FileUtils.createDirectory(path);
        } else {
            this.path = Paths.get("reformcloud/temp/servers/" + serverStartupInfo.getName() + "-" + serverStartupInfo.getUid());
            FileUtils.deleteFullDirectory(path);
            FileUtils.createDirectory(path);
        }
    }

    /**
     * Starts the SpigotServer
     *
     * @return {@code true} if the Client could start the SpigotServer
     * or {@code false} if the Client couldn't start the SpigotServer
     */
    public boolean bootstrap() {
        if (this.serverStartupInfo.getTemplate() != null)
            loaded = this.serverStartupInfo.getServerGroup().getTemplate(this.serverStartupInfo.getTemplate());
        else
            loaded = this.serverStartupInfo.getServerGroup().randomTemplate();

        this.processStartupStage = ProcessStartupStage.COPY;
        this.sendMessage(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_copies_template()
                .replace("%path%", this.path + ""));

        if (!this.serverStartupInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)) {
            if (loaded.getTemplateBackend().equals(TemplateBackend.URL)
                    && loaded.getTemplate_url() != null) {
                new TemplatePreparer(path + "/loaded.zip").loadTemplate(loaded.getTemplate_url());
                try {
                    ZoneInformationProtocolUtility.unZip(new File(path + "/loaded.zip"), path + "");
                } catch (final Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            } else if (loaded.getTemplateBackend().equals(TemplateBackend.CLIENT)) {
                FileUtils.copyAllFiles(Paths.get("reformcloud/templates/servers/" + serverStartupInfo.getServerGroup().getName() + "/" + this.loaded.getName()), path + StringUtil.EMPTY);
            } else if (loaded.getTemplateBackend().equals(TemplateBackend.CONTROLLER)) {
                ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController",
                        new PacketOutGetControllerTemplate("server",
                                this.serverStartupInfo.getServerGroup().getName(),
                                this.loaded.getName(),
                                this.serverStartupInfo.getUid(),
                                this.serverStartupInfo.getName())
                );
                ReformCloudLibraryService.sleep(50);
            } else
                return false;
        }

        FileUtils.copyAllFiles(Paths.get("libraries"), path + "/libraries");

        if (!Files.exists(Paths.get(path + "/plugins")))
            FileUtils.createDirectory(Paths.get(path + "/plugins"));

        if (!Files.exists(Paths.get(path + "/configs")) && this.serverStartupInfo
                .getServerGroup().getSpigotVersions().equals(SpigotVersions.SHORTSPIGOT_1_12_2)) {
            FileUtils.createDirectory(Paths.get(path + "/configs"));
        }

        FileUtils.createDirectory(Paths.get(path + "/reformcloud"));

        FileUtils.copyAllFiles(Paths.get("reformcloud/default/servers"), path + StringUtil.EMPTY);

        this.processStartupStage = ProcessStartupStage.PREPARING;
        if (serverStartupInfo.getServerGroup().getSpigotVersions().equals(SpigotVersions.SHORTSPIGOT_1_12_2)) {
            FileUtils.copyCompiledFile("reformcloud/spigot.yml", path + "/configs/spigot.yml");
            FileUtils.copyCompiledFile("reformcloud/server.properties", path + "/configs/server.properties");
        } else {
            FileUtils.copyCompiledFile("reformcloud/spigot.yml", path + "/spigot.yml");
            FileUtils.copyCompiledFile("reformcloud/server.properties", path + "/server.properties");
        }

        this.port = ReformCloudClient.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().nextFreePort(serverStartupInfo.getServerGroup().getStartPort());
        while (!ReformCloudClient.getInstance().isPortUseable(port)) {
            port++;
            ReformCloudLibraryService.sleep(20);
        }

        Properties properties = new Properties();
        if (serverStartupInfo.getServerGroup().getSpigotVersions().equals(SpigotVersions.SHORTSPIGOT_1_12_2)) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get(path + "/configs/server.properties")))) {
                properties.load(inputStreamReader);
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load server.properties", ex);
                return false;
            }
        } else {
            try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get(path + "/server.properties")))) {
                properties.load(inputStreamReader);
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load server.properties", ex);
                return false;
            }
        }

        properties.setProperty("server-ip", ReformCloudClient.getInstance().getCloudConfiguration().getStartIP());
        properties.setProperty("server-port", port + StringUtil.EMPTY);
        properties.setProperty("server-name", serverStartupInfo.getName());
        properties.setProperty("motd", serverStartupInfo.getServerGroup().getMotd());

        if (serverStartupInfo.getServerGroup().getSpigotVersions().equals(SpigotVersions.SHORTSPIGOT_1_12_2)) {
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(path + "/configs/server.properties"))) {
                properties.store(outputStream, "");
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Cannot store server.properties", ex);
                return false;
            }
        } else {
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(path + "/server.properties"))) {
                properties.store(outputStream, "");
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Cannot store server.properties", ex);
                return false;
            }
        }

        if (!Files.exists(Paths.get(path + "/spigot.jar"))) {
            if (!Files.exists(Paths.get("reformcloud/jars/" + SpigotVersions.getAsFormattedJarFileName(this.serverStartupInfo.getServerGroup().getSpigotVersions())))) {
                DownloadManager.downloadAndDisconnect(
                        this.serverStartupInfo.getServerGroup().getSpigotVersions().getName(),
                        this.serverStartupInfo.getServerGroup().getSpigotVersions().getUrl(),
                        "reformcloud/jars/" + SpigotVersions.getAsFormattedJarFileName(this.serverStartupInfo.getServerGroup().getSpigotVersions())
                );
            }

            FileUtils.copyFile("reformcloud/jars/" + SpigotVersions.getAsFormattedJarFileName(this.serverStartupInfo.getServerGroup().getSpigotVersions()), path + "/spigot.jar");
        }

        if (!Files.exists(Paths.get("reformcloud/apis/ReformAPISpigot-" + StringUtil.SPIGOT_API_DOWNLOAD + ".jar"))) {
            DownloadManager.downloadSilentAndDisconnect(
                    "https://dl.reformcloud.systems/apis/ReformAPISpigot-" + StringUtil.SPIGOT_API_DOWNLOAD + ".jar",
                    "reformcloud/apis/ReformAPISpigot-" + StringUtil.SPIGOT_API_DOWNLOAD + ".jar"
            );

            final File dir = new File("reformcloud/apis");
            if (dir.listFiles() != null) {
                Arrays.stream(dir.listFiles()).forEach(file -> {
                    if (file.getName().startsWith("ReformAPISpigot")
                            && file.getName().endsWith(".jar")
                            && !file.getName().contains(StringUtil.SPIGOT_API_DOWNLOAD)) {
                        file.delete();
                    }
                });
            }
        }

        FileUtils.deleteFileIfExists(Paths.get(path + "/plugins/ReformAPISpigot.jar"));
        FileUtils.copyFile("reformcloud/apis/ReformAPISpigot-" + StringUtil.SPIGOT_API_DOWNLOAD + ".jar", this.path + "/plugins/ReformAPISpigot.jar");

        ServerInfo serverInfo = new ServerInfo(
                new CloudProcess(serverStartupInfo.getName(), serverStartupInfo.getUid(), ReformCloudClient.getInstance().getCloudConfiguration().getClientName(),
                        loaded, serverStartupInfo.getId()),
                serverStartupInfo.getServerGroup(), serverStartupInfo.getServerGroup().getName(), ReformCloudClient.getInstance().getCloudConfiguration().getStartIP(),
                serverStartupInfo.getServerGroup().getMotd(), this.port, 0, serverStartupInfo.getServerGroup().getMemory(),
                false, new ArrayList<>()
        );

        new Configuration()
                .addProperty("info", serverInfo)
                .addProperty("address", ReformCloudClient.getInstance().getCloudConfiguration().getEthernetAddress())
                .addStringProperty("controllerKey", ReformCloudClient.getInstance().getCloudConfiguration().getControllerKey())
                .addBooleanProperty("ssl", ReformCloudClient.getInstance().isSsl())
                .addBooleanProperty("debug", ReformCloudClient.getInstance().getLoggerProvider().isDebug())
                .addProperty("startupInfo", serverStartupInfo)

                .write(Paths.get(path + "/reformcloud/config.json"));

        this.screenHandler = new ScreenHandler(serverInfo.getCloudProcess().getName());

        this.processStartupStage = ProcessStartupStage.START;
        final String[] cmd = new String[]
                {
                        StringUtil.JAVA,
                        "-XX:+UseG1GC",
                        "-XX:MaxGCPauseMillis=50",
                        "-XX:-UseAdaptiveSizePolicy",
                        "-XX:CompileThreshold=100",
                        "-Dcom.mojang.eula.agree=true",
                        "-Djline.terminal=jline.UnsupportedTerminal",
                        "-Xmx" + this.serverStartupInfo.getServerGroup().getMemory() + "M",
                        StringUtil.JAVA_JAR,
                        "spigot.jar",
                        "nogui"
                };

        try {
            this.process = Runtime.getRuntime().exec(cmd, null, new File(path + ""));
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Could not launch ServerStartup", ex);
            return false;
        }

        if (this.serverStartupInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)
                && loaded.getTemplateBackend().equals(TemplateBackend.CLIENT)
                && firstGroupStart) {
            FileUtils.copyAllFiles(path, "reformcloud/templates/servers/" + serverStartupInfo.getServerGroup().getName() + "/" + this.loaded.getName(), "spigot.jar");
        }

        ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().registerServerProcess(
                serverStartupInfo.getUid(), serverStartupInfo.getName(), serverInfo, port
        );
        ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutUpdateInternalCloudNetwork(ReformCloudClient.getInstance().getInternalCloudNetwork()));
        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutAddProcess(serverInfo));

        ReformCloudClient.getInstance().getCloudProcessScreenService().registerServerProcess(serverStartupInfo.getName(), this);
        ReformCloudClient.getInstance().getClientInfo().getStartedServers().add(serverInfo.getCloudProcess().getName());
        this.processStartupStage = ProcessStartupStage.DONE;
        return true;
    }

    /**
     * Checks if the ServerProcess is alive
     *
     * @return {@code true} if the ServerProcess is alive or {@code false} if the ServerProcess isn't alive
     * @see Process#isAlive()
     * @see Process#getInputStream()
     */
    public boolean isAlive() {
        try {
            return process != null && process.getInputStream().available() != -1 && process.isAlive();
        } catch (final Throwable throwable) {
            return false;
        }
    }

    /**
     * Stops the ServerProcess
     *
     * @return {@code true} if the Client could stop the SpigotProcess or
     * {@code false} if the Client couldn't stop the SpigotProcess
     * @see CloudServerStartupHandler#isAlive()
     */
    public boolean shutdown(final boolean update) {
        if (toShutdown)
            return true;

        toShutdown = true;
        ReformCloudClient.getInstance().getClientInfo().getStartedServers().remove(this.serverStartupInfo.getName());

        this.executeCommand("save-all");

        ReformCloudLibraryService.sleep(2000);

        this.executeCommand("stop");

        ReformCloudLibraryService.sleep(1000);

        try {
            if (this.isAlive()) {
                this.process.destroyForcibly().waitFor();
            }
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Error on CloudServer shutdown", throwable);
        }

        ReformCloudLibraryService.sleep(50);

        this.screenHandler.disableScreen();

        if (this.serverStartupInfo.getServerGroup().isSave_logs()) {
            FileUtils.copyFile(this.path + "/logs/latest.log", "reformcloud/saves/servers/logs/server_log_" + this.serverStartupInfo.getUid() + "-" + this.serverStartupInfo.getName() + ".log");
        }

        if (this.loaded.getTemplateBackend().equals(TemplateBackend.CONTROLLER)
                && !this.serverStartupInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)) {
            byte[] template = ZoneInformationProtocolUtility.zipDirectoryToBytes(this.path);
            ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized(
                    "ReformCloudController", new PacketOutUpdateControllerTemplate(
                            "server", this.serverStartupInfo.getServerGroup().getName(),
                            this.loaded.getName(), template
                    )
            );
        }

        if (!this.serverStartupInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC))
            FileUtils.deleteFullDirectory(path);

        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController",
                new PacketOutRemoveProcess(ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByUID(this.serverStartupInfo.getUid()))
        );

        ReformCloudClient.getInstance().getCloudProcessScreenService().unregisterServerProcess(this.serverStartupInfo.getName());
        ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterServerProcess(
                this.serverStartupInfo.getUid(), this.serverStartupInfo.getName(), this.port
        );

        if (update)
            ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutUpdateInternalCloudNetwork(ReformCloudClient.getInstance().getInternalCloudNetwork()));

        try {
            this.finalize();
        } catch (final Throwable ignored) {
        }

        return true;
    }

    /**
     * Executes a command on the SpigotProcess
     *
     * @param command
     * @see Process#getOutputStream()
     * @see OutputStream#write(byte[])
     */
    public void executeCommand(String command) {
        if (!this.isAlive()) return;

        try {
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
        } catch (final IOException ignored) {
        }
    }

    /**
     * Sends a message to ReformCloudController and to Client Console
     *
     * @param message
     * @see PacketOutSendControllerConsoleMessage
     */
    private void sendMessage(final String message) {
        ReformCloudClient.getInstance().getLoggerProvider().info(message);
        ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutSendControllerConsoleMessage(message));
    }

    public String uploadLog() throws IOException {
        if (!this.isAlive())
            return null;

        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(this.path + "/logs/latest.log"), StandardCharsets.UTF_8).forEach(e -> stringBuilder.append(e).append("\n"));

        return ReformCloudClient.getInstance().getLoggerProvider().uploadLog(stringBuilder.substring(0));
    }
}
