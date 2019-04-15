/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;

/**
 * @author _Klaro | Pasqual K. / created on 01.11.2018
 */

public final class NettySocketClient implements AutoCloseable {
    private SslContext sslContext;
    private final EventLoopGroup eventLoopGroup = ReformCloudLibraryService.eventLoopGroup(4);

    /**
     * Connects to the ReformCloudController
     *
     * @param ethernetAddress
     * @param channelHandler
     * @param ssl
     * @param key
     * @param name
     */
    public void connect(EthernetAddress ethernetAddress, ChannelHandler channelHandler, boolean ssl, String key, String name) {
        try {
            if (ssl)
                sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)

                    .option(ChannelOption.AUTO_READ, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.IP_TOS, 24)
                    .option(ChannelOption.TCP_NODELAY, true)

                    .channel(ReformCloudLibraryService.clientSocketChannel())

                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel channel) {
                            if (ssl && sslContext != null)
                                channel.pipeline().addLast(sslContext.newHandler(channel.alloc(),
                                        ethernetAddress.getHost(), ethernetAddress.getPort()));

                            ReformCloudLibraryService.prepareChannel(channel, channelHandler);
                        }
                    });

            bootstrap.connect(ethernetAddress.getHost(), ethernetAddress.getPort()).sync().channel().writeAndFlush(new Packet("Auth",
                    new Configuration()
                            .addStringProperty("key", key)
                            .addStringProperty("name", name)
                            .addProperty("AuthenticationType", AuthenticationType.PROXY)
            ));
        } catch (final Throwable ignored) {
        }
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
