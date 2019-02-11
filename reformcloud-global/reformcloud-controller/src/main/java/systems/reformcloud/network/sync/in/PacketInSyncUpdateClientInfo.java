/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class PacketInSyncUpdateClientInfo implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -8551248371013672598L;

    @Override
    public void handle(Configuration configuration) {
        Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(configuration.getStringValue("from"));
        if (client == null)
            return;

        client.setClientInfo(configuration.getValue("info", TypeTokenAdaptor.getClientInfoType()));
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }
}
