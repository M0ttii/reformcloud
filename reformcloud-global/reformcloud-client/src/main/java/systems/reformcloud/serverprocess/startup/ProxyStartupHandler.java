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
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.stages.ProcessStartupStage;
import systems.reformcloud.network.packets.out.*;
import systems.reformcloud.serverprocess.screen.ScreenHandler;
import systems.reformcloud.template.TemplatePreparer;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

@Getter
public class ProxyStartupHandler {
    private ProxyStartupInfo proxyStartupInfo;
    private Path path;
    private Process process;
    private int port;

    private boolean toShutdown = false;

    private ScreenHandler screenHandler;
    private ProcessStartupStage processStartupStage;

    private Template template;

    /**
     * Creates a instance of a ProxyStartupHandler
     *
     * @param proxyStartupInfo
     */
    public ProxyStartupHandler(final ProxyStartupInfo proxyStartupInfo) {
        this.processStartupStage = ProcessStartupStage.WAITING;
        this.proxyStartupInfo = proxyStartupInfo;
        this.path = Paths.get("reformcloud/temp/proxies/" + proxyStartupInfo.getName() + "-" + proxyStartupInfo.getUid());
    }

    /**
     * Starts the BungeeCord
     *
     * @return {@code true} if the Client could start the BungeeCord
     * or {@code false} if the Client couldn't start the BungeeCord
     */
    public boolean bootstrap() {
        FileUtils.deleteFullDirectory(path);

        FileUtils.copyAllFiles(Paths.get("libraries"), path + "/libraries");

        if (this.proxyStartupInfo.getTemplate() != null)
            template = this.proxyStartupInfo.getProxyGroup().getTemplate(this.proxyStartupInfo.getTemplate());
        else
            template = this.proxyStartupInfo.getProxyGroup().randomTemplate();

        this.processStartupStage = ProcessStartupStage.COPY;
        this.sendMessage(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_copies_template()
                .replace("%path%", this.path + ""));
        if (template.getTemplateBackend().equals(TemplateBackend.URL)
                && template.getTemplate_url() != null) {
            new TemplatePreparer(path + "/template.zip").loadTemplate(template.getTemplate_url());
            try {
                ZoneInformationProtocolUtility.unZip(new File(path + "/template.zip"), path + "");
            } catch (final Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } else if (template.getTemplateBackend().equals(TemplateBackend.CLIENT)) {
            FileUtils.copyAllFiles(Paths.get("reformcloud/templates/" + proxyStartupInfo.getProxyGroup().getName()), path + StringUtil.EMPTY);
        } else {
            return false;
        }

        if (!Files.exists(Paths.get(path + "/plugins")))
            FileUtils.createDirectory(Paths.get(path + "/plugins"));

        FileUtils.createDirectory(Paths.get(path + "/reformcloud"));

        FileUtils.copyAllFiles(Paths.get("reformcloud/default/proxies"), path + StringUtil.EMPTY);

        this.processStartupStage = ProcessStartupStage.PREPARING;
        this.port = ReformCloudClient.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().nextFreePort(proxyStartupInfo.getProxyGroup().getStartPort());
        while (!ReformCloudClient.getInstance().isPortUseable(port)) {
            port++;
            ReformCloudLibraryService.sleep(20);
        }

        if (!Files.exists(Paths.get(path + "/server-icon.png")))
            FileUtils.copyCompiledFile("reformcloud/server-icon.png", path + "/server-icon.png");

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path + "/server-icon.png"));
            if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
                ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController",
                        new PacketOutIconSizeIncorrect(this.proxyStartupInfo.getName()));
                FileUtils.deleteFileIfExists(Paths.get(path + "/server-icon.png"));
                FileUtils.copyCompiledFile("reformcloud/server-icon.png", path + "/server-icon.png");
            }
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Error while reading image", ex);
            return false;
        }

        FileUtils.deleteFileIfExists(Paths.get(path + "/config.yml"));
        FileUtils.copyCompiledFile("reformcloud/config.yml", path + "/config.yml");

        if (!Files.exists(Paths.get(path + "/BungeeCord.jar"))) {
            if (!Files.exists(Paths.get("reformcloud/jars/" + ProxyVersions.getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions())))) {
                DownloadManager.downloadAndDisconnect(
                        this.proxyStartupInfo.getProxyGroup().getProxyVersions().getName(),
                        this.proxyStartupInfo.getProxyGroup().getProxyVersions().getUrl(),
                        "reformcloud/jars/" + ProxyVersions.getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions())
                );
            }

            FileUtils.copyFile("reformcloud/jars/" + ProxyVersions.getAsJarFileName(this.proxyStartupInfo.getProxyGroup().getProxyVersions()), path + "/BungeeCord.jar");
        }

        if (!Files.exists(Paths.get("reformcloud/apis/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar"))) {
            DownloadManager.downloadSilentAndDisconnect(
                    "https://reformcloud.systems/download/latest/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar",
                    "reformcloud/apis/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar"
            );

            final File dir = new File("reformcloud/apis");
            if (dir.listFiles() != null) {
                Arrays.stream(dir.listFiles()).forEach(file -> {
                    if (file.getName().startsWith("ReformAPIBungee")
                            && file.getName().endsWith(".jar")
                            && !file.getName().contains(StringUtil.BUNGEE_API_DOWNLOAD)) {
                        file.delete();
                    }
                });
            }
        }

        FileUtils.deleteFileIfExists(Paths.get(path + "/plugins/ReformAPIBungee.jar"));
        FileUtils.copyFile("reformcloud/apis/ReformAPIBungee-" + StringUtil.BUNGEE_API_DOWNLOAD + ".jar", this.path + "/plugins/ReformAPIBungee.jar");

        try {
            this.prepareConfiguration(new File(path + "/config.yml"), "\"" +
                    ReformCloudClient.getInstance().getCloudConfiguration().getStartIP() + ":" + port + "\"");
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while preparing proxy configuration, break", throwable);
            return false;
        }

        ProxyInfo proxyInfo = new ProxyInfo(
                new CloudProcess(proxyStartupInfo.getName(), proxyStartupInfo.getUid(), ReformCloudClient.getInstance().getCloudConfiguration().getClientName(),
                        template, proxyStartupInfo.getId()),
                proxyStartupInfo.getProxyGroup(), proxyStartupInfo.getProxyGroup().getName(), ReformCloudClient.getInstance().getCloudConfiguration().getStartIP(),
                this.port, 0, proxyStartupInfo.getProxyGroup().getMemory(),false, new ArrayList<>()
        );

        new Configuration()
                .addProperty("info", proxyInfo)
                .addProperty("address", ReformCloudClient.getInstance().getCloudConfiguration().getEthernetAddress())
                .addStringProperty("controllerKey", ReformCloudClient.getInstance().getCloudConfiguration().getControllerKey())
                .addBooleanProperty("ssl", ReformCloudClient.getInstance().getNettySocketClient().getSslContext() != null)
                .addProperty("startupInfo", proxyStartupInfo)

                .write(Paths.get(path + "/reformcloud/config.json"));

        this.screenHandler = new ScreenHandler(proxyInfo.getCloudProcess().getName());

        this.processStartupStage = ProcessStartupStage.START;
        final String[] cmd = new String[]
                {
                        StringUtil.JAVA,
                        "-XX:+UseG1GC",
                        "-XX:MaxGCPauseMillis=50",
                        "-XX:-UseAdaptiveSizePolicy",
                        "-XX:CompileThreshold=100",
                        "-Djline.terminal=jline.UnsupportedTerminal",
                        "-Xmx" + this.proxyStartupInfo.getProxyGroup().getMemory() + "M",
                        StringUtil.JAVA_JAR,
                        "BungeeCord.jar"
                };

        try {
            this.process = Runtime.getRuntime().exec(cmd, null, new File(path.toString()));
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while starting proxy process", ex);
            return false;
        }

        ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().registerProxyProcess(
                proxyStartupInfo.getUid(), proxyStartupInfo.getName(), proxyInfo, port
        );
        ReformCloudClient.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutUpdateInternalCloudNetwork(ReformCloudClient.getInstance().getInternalCloudNetwork()), new PacketOutAddProcess(proxyInfo));

        ReformCloudClient.getInstance().getCloudProcessScreenService().registerProxyProcess(proxyStartupInfo.getName(), this);
        ReformCloudClient.getInstance().getClientInfo().getStartedProxies().add(proxyInfo.getCloudProcess().getName());
        this.processStartupStage = ProcessStartupStage.DONE;
        return true;
    }

    /**
     * Checks if the BungeeCordProcess is alive
     *
     * @return {@code true} if the BungeeCordProcess is alive or {@code false} if the BungeeCordProcess isn't alive
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
     * Stops the Process
     *
     * @param message
     * @return {@code true} if the Client could stop the Process or
     * {@code false} if the Client couldn't stop the Process
     * @see ProxyStartupHandler#isAlive()
     */
    public boolean shutdown(final String message, final boolean update) {
        if (toShutdown)
            return true;

        toShutdown = true;
        ReformCloudClient.getInstance().getClientInfo().getStartedProxies().remove(this.proxyStartupInfo.getName());

        if (message == null)
            this.executeCommand("end ReformCloud restarting...");
        else
            this.executeCommand(message.startsWith(" ") ? "end" + message : "end " + message);

        try {
            if (this.isAlive()) {
                this.process.destroyForcibly().waitFor();
            }
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Error on Proxy shutdown", throwable);
        }

        ReformCloudLibraryService.sleep(50);

        this.screenHandler.disableScreen();

        if (this.proxyStartupInfo.getProxyGroup().isSave_logs()) {
            if (this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.BUNGEECORD)
                    || this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.HEXACORD)) {
                FileUtils.copyFile(this.path + "/proxy.log.0", "reformcloud/saves/proxies/logs/server_log_" + this.proxyStartupInfo.getUid() + "-" + this.proxyStartupInfo.getName() + ".log");
            } else if (this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.TRAVERTINE)
                    || this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.WATERFALL)) {
                FileUtils.copyFile(this.path + "/logs/latest.log", "reformcloud/saves/proxies/logs/server_log_" + this.proxyStartupInfo.getUid() + "-" + this.proxyStartupInfo.getName() + ".log");
            }
        }

        FileUtils.deleteFullDirectory(path);

        final ProxyInfo proxyInfo = ReformCloudClient.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getRegisteredProxyByUID(this.proxyStartupInfo.getUid());

        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController",
                new PacketOutRemoveProcess(proxyInfo));

        ReformCloudClient.getInstance().getCloudProcessScreenService().unregisterProxyProcess(this.proxyStartupInfo.getName());
        ReformCloudClient.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterProxyProcess(
                this.proxyStartupInfo.getUid(), this.proxyStartupInfo.getName(), this.port
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
     * Executes a command on the BungeeCordProcess
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
     * Prepares the configuration for the BungeeCord startup
     *
     * @param file
     * @param hostAndPort
     * @throws Throwable
     */
    private void prepareConfiguration(final File file, final String hostAndPort) throws Throwable {
        String context = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        context = context.replace("0.0.0.0:25577", hostAndPort);
        org.apache.commons.io.FileUtils.write(file, context, StandardCharsets.UTF_8);
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

        if (this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.BUNGEECORD)
                || this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.HEXACORD)) {
            Files.readAllLines(Paths.get(this.path + "/proxy.log.0"), StandardCharsets.UTF_8).forEach(e -> stringBuilder.append(e).append("\n"));
        } else if (this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.TRAVERTINE)
                || this.proxyStartupInfo.getProxyGroup().getProxyVersions().equals(ProxyVersions.WATERFALL)) {
            Files.readAllLines(Paths.get(this.path + "/logs/latest.log"), StandardCharsets.UTF_8).forEach(e -> stringBuilder.append(e).append("\n"));
        }

        return ReformCloudClient.getInstance().getLoggerProvider().uploadLog(stringBuilder.substring(0));
    }
}
