/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudProxyRemoveEvent;
import systems.reformcloud.internal.events.CloudServerRemoveEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public class PacketInProcessRemove implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());
            SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new CloudServerRemoveEvent(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getProxyInfoType());
            SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new CloudProxyRemoveEvent(proxyInfo));
        }
    }
}
