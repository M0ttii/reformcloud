/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.signs.Sign;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutCreateSign extends Packet {

    public PacketOutCreateSign(final Sign sign) {
        super("CreateSign", new Configuration().addValue("sign", sign));
    }
}
