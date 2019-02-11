/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class PacketOutStartProxy extends Packet {
    public PacketOutStartProxy(final ProxyGroup group, final String processName, final UUID proxyProcess, final Configuration configuration, final String id) {
        super("StartProxy",
                new Configuration().addProperty("group", group).addStringProperty("name", processName).addProperty("proxyProcess", proxyProcess).addConfigurationProperty("preConfig", configuration).addIntegerProperty("id", Integer.valueOf(id))
        );
        ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_proxy_added_to_queue()
                .replace("%uid%", String.valueOf(proxyProcess))
                .replace("%name%", processName));
    }

    public PacketOutStartProxy(final ProxyGroup group, final String processName, final UUID proxyProcess, final Configuration configuration, final String id, final String template) {
        super("StartProxy",
                new Configuration().addProperty("group", group).addStringProperty("name", processName).addStringProperty("template", template).addProperty("proxyProcess", proxyProcess).addConfigurationProperty("preConfig", configuration).addIntegerProperty("id", Integer.valueOf(id))
        );
        ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_proxy_added_to_queue()
                .replace("%uid%", String.valueOf(proxyProcess))
                .replace("%name%", processName));
    }
}
