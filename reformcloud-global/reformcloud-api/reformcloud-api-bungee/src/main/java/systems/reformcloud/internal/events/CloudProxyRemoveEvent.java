/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import net.md_5.bungee.api.plugin.Event;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * This class represents the remove of a cloud proxy
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class CloudProxyRemoveEvent extends Event implements Serializable {
    private ProxyInfo proxyInfo;

    @java.beans.ConstructorProperties({"proxyInfo"})
    public CloudProxyRemoveEvent(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }
}
