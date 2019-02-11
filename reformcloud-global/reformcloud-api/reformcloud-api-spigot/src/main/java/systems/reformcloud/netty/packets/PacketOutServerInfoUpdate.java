/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutServerInfoUpdate extends Packet {
    public PacketOutServerInfoUpdate(final ServerInfo serverInfo) {
        super("ServerInfoUpdate", new Configuration().addProperty("serverInfo", serverInfo));
    }
}
