/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ProxyAddon;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class ProxyReloadCommand extends Command implements Serializable {
    public ProxyReloadCommand() {
        super("proxy", "Manage the settings of a specific proxygroup", "reformcloud.commands.proxy", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            ProxyAddon.getInstance().getProxyAddonConfiguration().reload();
        } else {
            commandSender.sendMessage("proxy reload");
        }
    }
}
