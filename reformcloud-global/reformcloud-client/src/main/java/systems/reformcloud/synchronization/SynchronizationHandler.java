/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.synchronization;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncUpdateClientInfo;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class SynchronizationHandler implements Serializable, Runnable {

    private static final long serialVersionUID = -7527313886584796220L;

    private boolean deleted = false;

    private ClientInfo lastInfo = ReformCloudClient.getInstance().getClientInfo();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !deleted) {
            ClientInfo current = ReformCloudClient.getInstance().getClientInfo();

            double cpuUsage = ReformCloudLibraryService.cpuUsage();
            long memory = ReformCloudLibraryService.usedMemorySystem();
            int internalMemory = ReformCloudClient.getInstance().getMemory();
            int startedServerCount = current.getStartedServers().size();
            int startedProxyCount = current.getStartedProxies().size();

            if (lastInfo.getCpuUsage() != cpuUsage
                || lastInfo.getSystemMemoryUsage() != memory
                || lastInfo.getUsedMemory() != internalMemory
                || lastInfo.getStartedServers().size() != startedServerCount
                || lastInfo.getStartedProxies().size() != startedProxyCount) {
                lastInfo.setCpuUsage(cpuUsage);
                lastInfo.setSystemMemoryUsage(memory);
                lastInfo.setUsedMemory(internalMemory);
                lastInfo.setStartedProxies(current.getStartedProxies());
                lastInfo.setStartedServers(current.getStartedServers());

                ReformCloudClient.getInstance().setClientInfo(lastInfo);
                lastInfo = current;

                ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous(
                    "ReformCloudController", new PacketOutSyncUpdateClientInfo(lastInfo)
                );
            }

            ReformCloudLibraryService.sleep(TimeUnit.SECONDS, 10);
        }
    }

    public void delete() {
        this.deleted = true;
    }

    public void setLastInfo(ClientInfo lastInfo) {
        this.lastInfo = lastInfo;
    }
}
