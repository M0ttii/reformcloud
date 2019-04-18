/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.PlayerDisconnectsEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketInLogoutPlayer implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getUuid().remove(configuration.getValue("uuid", UUID.class));
        ReformCloudController.getInstance().getPlayerDatabase().logoutPlayer(configuration.getValue("uuid", UUID.class));
        ReformCloudController.getInstance().getEventManager().callEvent(new PlayerDisconnectsEvent(configuration.getValue("uuid", UUID.class)));
    }
}
