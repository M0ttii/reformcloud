/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class PacketOutUpdateProxyConfig extends Packet implements Serializable {
    public PacketOutUpdateProxyConfig(Optional<ProxySettings> proxySettings) {
        super("UpdateProxyConfig", new Configuration().addProperty("settings", proxySettings));
    }
}
