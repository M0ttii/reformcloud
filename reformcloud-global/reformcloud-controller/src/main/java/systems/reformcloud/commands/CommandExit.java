/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class CommandExit extends Command implements Serializable {
    public CommandExit() {
        super("exit", "Stops the System", "reformcloud.command.exit", new String[]{"kill", "end", "stop", "shutdown"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("ReformCloud will stop...");
        System.exit(1);
    }
}
