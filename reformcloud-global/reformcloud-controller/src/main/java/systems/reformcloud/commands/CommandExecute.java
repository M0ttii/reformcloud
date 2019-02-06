/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.netty.out.PacketOutExecuteCommand;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.12.2018
 */

public final class CommandExecute extends Command implements Serializable {
    public CommandExecute() {
        super("execute", "Executes a command on a server or proxy", "reformcloud.command.execute", new String[]{"exec"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length < 3) {
            commandSender.sendMessage("execute <server/proxy> <name/--all> <command>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "server": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]) != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (short i = 2; i < args.length; i++)
                        stringBuilder.append(args[i]).append(StringUtil.SPACE);

                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]).getCloudProcess().getClient(), new PacketOutExecuteCommand(stringBuilder.substring(0, stringBuilder.length() - 1), args[0].toLowerCase(), args[1]));
                    commandSender.sendMessage("The command has been executed.");
                } else if (args[1].equalsIgnoreCase("--all")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (short i = 2; i < args.length; i++)
                        stringBuilder.append(args[i]).append(StringUtil.SPACE);

                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredServerProcesses().forEach(e -> {
                        ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(e.getCloudProcess().getClient(), new PacketOutExecuteCommand(stringBuilder.substring(0, stringBuilder.length() - 1), args[0].toLowerCase(), e.getCloudProcess().getName()));
                    });

                    commandSender.sendMessage("The command has been executed.");
                } else
                    commandSender.sendMessage("This Server is not connected to controller");
                break;
            }
            case "proxy": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]) != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (short i = 2; i < args.length; i++)
                        stringBuilder.append(args[i]).append(StringUtil.SPACE);

                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]).getCloudProcess().getClient(), new PacketOutExecuteCommand(stringBuilder.substring(0, stringBuilder.length() - 1), args[0].toLowerCase(), args[1]));
                    commandSender.sendMessage("The command has been executed.");
                } else if (args[1].equalsIgnoreCase("--all")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (short i = 2; i < args.length; i++)
                        stringBuilder.append(args[i]).append(StringUtil.SPACE);

                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredProxyProcesses().forEach(e -> {
                        ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(e.getCloudProcess().getClient(), new PacketOutExecuteCommand(stringBuilder.substring(0, stringBuilder.length() - 1), args[0].toLowerCase(), e.getCloudProcess().getName()));
                    });

                    commandSender.sendMessage("The command has been executed.");
                } else
                    commandSender.sendMessage("This Proxy is not connected to controller");
                break;
            }
            default: {
                commandSender.sendMessage("execute <server/proxy> <name> <command>");
            }
        }
    }
}
