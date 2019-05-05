/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketInSyncScreenUpdate implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -7529295495170339243L;

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getScreenSessionProvider().sendScreenMessage(
                configuration.getStringValue("line"), configuration.getStringValue("from")
        );
    }
}
