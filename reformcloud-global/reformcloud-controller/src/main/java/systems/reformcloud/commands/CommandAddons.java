/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class CommandAddons extends Command implements Serializable {
    public CommandAddons() {
        super("addons", "List, enable and disable addons", "reformcloud.command.addons", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (ReformCloudController.getInstance().getAddonParallelLoader().getJavaAddons().size() == 0) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage().getCommand_addons_no_addons_loaded());
            } else {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage().getCommand_addons_following_loaded());
                ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                ReformCloudController.getInstance().getAddonParallelLoader()
                        .getJavaAddons()
                        .forEach(e -> commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage().getCommand_addons_addon_description()
                                        .replace("%name%", e.getAddonName())
                                        .replace("%version%", e.getAddonClassConfig().getVersion())
                                        .replace("%main%", e.getAddonClassConfig().getMain())
                        ));
            }
        } else {
            commandSender.sendMessage("addons list");
        }
    }
}
