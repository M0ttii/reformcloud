/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel.queue;

import lombok.AllArgsConstructor;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.packet.AwaitingPacket;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

@AllArgsConstructor
public final class QueueWorker implements Serializable, Runnable {
    private final ChannelHandler instance = ReformCloudLibraryServiceProvider.getInstance().getChannelHandler();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!instance.getPacketQueue().isEmpty()) {
                AwaitingPacket awaitingPacket = instance.getPacketQueue().poll();
                if (!awaitingPacket.getChannelHandlerContext().channel().isWritable()) {
                    instance.getPacketQueue().offer(awaitingPacket);
                }

                instance.sendPacket(awaitingPacket);
            }

            ReformCloudLibraryService.sleep(TimeUnit.MILLISECONDS, 10);
        }
    }
}
