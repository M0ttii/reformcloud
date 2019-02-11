/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packets.sync.out.PacketOutSyncUpdateClientInfo;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class PacketInInitializeInternal implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudClient.getInstance().getLoggerProvider().info("NetworkProperties are now set and ReformCloudClient is now ready");

        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutSyncUpdateClientInfo(ReformCloudClient.getInstance().getClientInfo()));

        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess",
                new Configuration().addStringProperty("name", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
        ));
    }
}