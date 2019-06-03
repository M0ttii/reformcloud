/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 30.11.2018
 */

public final class PacketInCommandExecute implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getLoggerProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage().getController_command_executed()
                .replace("%name%", configuration.getStringValue("name"))
                .replace("%uuid%", String.valueOf(configuration.getValue("uuid", UUID.class)))
                .replace("%proxy%", configuration.getStringValue("proxyName"))
                .replace("%command%", configuration.getStringValue("command"))
                .replace("%server%", configuration.getStringValue("server")));
        ReformCloudController.getInstance().getStatisticsProvider().addIngameCommand();
    }
}
