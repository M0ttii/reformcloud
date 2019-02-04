/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.packets.PacketOutCheckPlayer;
import systems.reformcloud.netty.packets.PacketOutServerInfoUpdate;
import systems.reformcloud.netty.packets.PacketOutStartGameServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PlayerConnectListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void handle(final AsyncPlayerPreLoginEvent event) {
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutCheckPlayer(event.getUniqueId()));
        ReformCloudLibraryService.sleep(25);
        if (!SpigotBootstrap.getInstance().getAcceptedPlayers().contains(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-connect-only-proxy"));
            return;
        }

        if (ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup().isMaintenance()
                && SpigotBootstrap.getInstance().getServer().getPlayer(event.getUniqueId()) != null
                && !SpigotBootstrap.getInstance().getServer().getPlayer(event.getUniqueId()).hasPermission("reformcloud.join.server.maintenance")) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-connect-no-permission"));
            return;
        }

        final ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        List<UUID> online = serverInfo.getOnlinePlayers();
        online.add(event.getUniqueId());

        serverInfo.setOnlinePlayers(online);
        serverInfo.setOnline(online.size());

        if (online.size() <= serverInfo.getServerGroup().getMaxPlayers())
            serverInfo.setFull(true);
        else
            serverInfo.setFull(false);

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutServerInfoUpdate(serverInfo));

        event.allow();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(final PlayerQuitEvent event) {
        final ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        List<UUID> online = serverInfo.getOnlinePlayers();
        online.remove(event.getPlayer().getUniqueId());

        serverInfo.setOnlinePlayers(online);
        serverInfo.setOnline(online.size());

        if (online.size() <= serverInfo.getServerGroup().getMaxPlayers())
            serverInfo.setFull(true);
        else
            serverInfo.setFull(false);

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutServerInfoUpdate(serverInfo));
    }
}
