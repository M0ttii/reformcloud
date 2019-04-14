/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.synchronization;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncUpdateClientInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class SynchronizationHandler implements Serializable, Job {
    private static final long serialVersionUID = -7527313886584796220L;

    private ClientInfo lastInfo = ReformCloudClient.getInstance().getClientInfo();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
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

            ReformCloudClient.getInstance().setClientInfo(lastInfo);
            lastInfo = current;

            ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous(
                    "ReformCloudController", new PacketOutSyncUpdateClientInfo(lastInfo)
            );
        }
    }
}
