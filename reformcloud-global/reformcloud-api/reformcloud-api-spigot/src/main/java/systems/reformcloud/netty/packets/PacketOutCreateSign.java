/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.signs.Sign;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutCreateSign extends Packet {
    public PacketOutCreateSign(final Sign sign) {
        super("CreateSign", new Configuration().addProperty("sign", sign), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }
}
