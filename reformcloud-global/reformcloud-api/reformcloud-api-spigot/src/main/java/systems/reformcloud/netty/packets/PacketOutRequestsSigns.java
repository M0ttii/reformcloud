/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutRequestsSigns extends Packet {
    public PacketOutRequestsSigns() {
        super("RequestSigns", new Configuration().addStringProperty("name", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()));
    }
}
