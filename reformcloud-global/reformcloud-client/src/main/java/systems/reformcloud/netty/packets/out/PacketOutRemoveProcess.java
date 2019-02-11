/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketOutRemoveProcess extends Packet {
    public PacketOutRemoveProcess(final ServerInfo serverInfo) {
        super("ProcessRemove", new Configuration().addProperty("serverInfo", serverInfo));
    }

    public PacketOutRemoveProcess(final ProxyInfo proxyInfo) {
        super("ProcessRemove", new Configuration().addProperty("proxyInfo", proxyInfo));
    }
}
