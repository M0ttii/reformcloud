/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketOutLogoutPlayer extends Packet {
    public PacketOutLogoutPlayer(final UUID uuid) {
        super("LogoutPlayer", new Configuration().addProperty("uuid", uuid));
    }
}
