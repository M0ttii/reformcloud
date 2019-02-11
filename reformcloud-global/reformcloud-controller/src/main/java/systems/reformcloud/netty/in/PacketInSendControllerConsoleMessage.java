/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public class PacketInSendControllerConsoleMessage implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getLoggerProvider().info(configuration.getStringValue("message"));
    }
}
