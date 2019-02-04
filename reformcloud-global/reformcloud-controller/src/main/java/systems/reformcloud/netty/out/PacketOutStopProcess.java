/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketOutStopProcess extends Packet {
    public PacketOutStopProcess(final String name) {
        super("StopProcess", new Configuration().addStringProperty("name", name), Collections.singletonList(QueryType.COMPLETE), PacketSender.CONTROLLER);
    }
}
