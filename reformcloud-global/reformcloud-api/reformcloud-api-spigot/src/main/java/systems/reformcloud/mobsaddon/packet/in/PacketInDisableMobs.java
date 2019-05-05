/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon.packet.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobsaddon.MobSelector;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketInDisableMobs implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (MobSelector.getInstance() != null)
            MobSelector.getInstance().close();
    }
}
