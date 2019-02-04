/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandClear implements Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        try {
            ReformCloudController.getInstance().getLoggerProvider().getConsoleReader().clearScreen();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error in clear command", ex);
        }
    }

    @Override
    public final String getPermission() {
        return "reformcloud.command.clear";
    }
}
