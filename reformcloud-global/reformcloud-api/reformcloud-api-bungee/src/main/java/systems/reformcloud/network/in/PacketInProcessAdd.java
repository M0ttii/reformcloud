/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import net.md_5.bungee.api.ProxyServer;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudProxyAddEvent;
import systems.reformcloud.internal.events.CloudServerAddEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public class PacketInProcessAdd implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());
            final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getProxyGroups().get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());

            if (proxyGroup == null || proxyGroup.getDisabledServerGroups().contains(serverInfo.getServerGroup().getName()))
                return;

            ProxyServer.getInstance().getPluginManager().callEvent(new CloudServerAddEvent(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getProxyInfoType());
            ProxyServer.getInstance().getPluginManager().callEvent(new CloudProxyAddEvent(proxyInfo));
        }
    }
}
