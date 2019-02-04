/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketOutProcessRemove extends Packet {
    public PacketOutProcessRemove(final ServerInfo serverInfo) {
        super("ProcessRemove", new Configuration().addProperty("serverInfo", serverInfo), Collections.singletonList(QueryType.COMPLETE), PacketSender.CONTROLLER);
    }

    public PacketOutProcessRemove(final ProxyInfo proxyInfo) {
        super("ProcessRemove", new Configuration().addProperty("proxyInfo", proxyInfo), Collections.singletonList(QueryType.COMPLETE), PacketSender.CONTROLLER);
    }
}
