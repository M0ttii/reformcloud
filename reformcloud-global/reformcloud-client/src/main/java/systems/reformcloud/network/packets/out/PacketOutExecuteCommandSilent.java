/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.05.2019
 */

public final class PacketOutExecuteCommandSilent extends Packet implements Serializable {
    public PacketOutExecuteCommandSilent(String line) {
        super("ExecuteCommandSilent", new Configuration().addStringValue("line", line));
    }
}
