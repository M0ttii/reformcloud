/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.CommandManager;
import systems.reformcloud.libloader.LibraryLoader;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.time.DateProvider;
import io.netty.util.ResourceLeakDetector;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.10.2018
 */

final class ReformCloudLauncher {
    /**
     * Main Method of ReformCloudClient
     *
     * @param args          The given args by the executor
     * @throws Throwable    Will be thrown if an error occurs
     */
    public static synchronized void main(String[] args) throws Throwable {
        final long current = System.currentTimeMillis();

        System.out.println("Trying to startup ReformCloudClient...");
        System.out.println("Startup time: " + DateProvider.formatByDefaultFormat(current));

        new LibraryLoader().loadJarFileAndInjectLibraries();

        if (Files.exists(Paths.get("reformcloud/logs")))
            FileUtils.deleteFullDirectory(Paths.get("reformcloud/logs"));

        System.out.println();

        ReformCloudLibraryService.sendHeader();

        final List<String> options = Arrays.asList(args);

        final LoggerProvider loggerProvider = new LoggerProvider();
        final CommandManager commandManager = new CommandManager();

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        new ReformCloudClient(loggerProvider, commandManager, options.contains("--ssl"), current);

        loggerProvider.info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getHelp_default());

        String line;
        try {
            while (true) {
                loggerProvider.getConsoleReader().setPrompt("");
                loggerProvider.getConsoleReader().resetPromptLine("", "", 0);

                while ((line = loggerProvider.getConsoleReader().readLine(StringUtil.REFORM_VERSION + "-" + StringUtil.REFORM_SPECIFICATION + "@ReformCloudClient > ")) != null && ReformCloudClient.RUNNING) {
                    loggerProvider.getConsoleReader().setPrompt("");

                    if (!commandManager.dispatchCommand(line))
                        loggerProvider.info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getHelp_command_not_found());
                }
            }
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while handling command", throwable);
        }
    }
}
