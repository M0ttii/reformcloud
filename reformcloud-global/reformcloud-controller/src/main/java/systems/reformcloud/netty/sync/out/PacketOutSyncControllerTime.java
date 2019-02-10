/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class PacketOutSyncControllerTime extends Packet implements Serializable {
    public PacketOutSyncControllerTime() {
        super(
                "SyncControllerTime",
                new Configuration().addLongProperty("time", System.currentTimeMillis()),
                Arrays.asList(QueryType.COMPLETE),
                PacketSender.CONTROLLER
        );
    }
}
