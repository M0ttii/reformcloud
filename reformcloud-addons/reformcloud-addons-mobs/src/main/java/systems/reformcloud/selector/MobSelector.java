/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.selector;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobs.config.SelectorMobConfig;
import systems.reformcloud.mobs.inventory.SelectorMobInventory;
import systems.reformcloud.mobs.inventory.SelectorMobInventoryItem;
import systems.reformcloud.mobs.inventory.item.SelectorsMobServerItem;
import systems.reformcloud.packets.in.PacketInCreateMob;
import systems.reformcloud.packets.in.PacketInDeleteMob;
import systems.reformcloud.packets.in.PacketInQueryGetAll;
import systems.reformcloud.packets.out.*;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class MobSelector implements Serializable {

    private static MobSelector instance;

    private final String directory = "reformcloud/addons/mobs";

    private final String databaseDir = "reformcloud/database/mobs";

    private Map<UUID, SelectorMob> mobs = new HashMap<>();

    private SelectorMobConfig selectorMobConfig;

    public MobSelector() {
        if (instance != null) {
            throw new InstanceAlreadyExistsException();
        }

        instance = this;

        this.defaultInit();
        this.registerNetworkHandlers();
        this.reload();
    }

    public static MobSelector getInstance() {
        return MobSelector.instance;
    }

    private void defaultInit() {
        if (!Files.exists(Paths.get(directory))) {
            FileUtils.createDirectory(Paths.get(directory));
            FileUtils.createDirectory(Paths.get(databaseDir));
        }
    }

    private void reload() {
        if (!Files.exists(Paths.get(directory + "/config.json"))) {
            new Configuration().addValue("config", new SelectorMobConfig(
                new SelectorMobInventory(
                    "§7» §a%group_name%",
                    54,
                    Arrays.asList(
                        new SelectorMobInventoryItem(" ", "TORCH", 0, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 1, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 2, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 3, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 4, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 5, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 6, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 7, (short) 0),
                        new SelectorMobInventoryItem(" ", "TORCH", 8, (short) 0)
                    )
                ), new SelectorsMobServerItem("§a%server_name%", "CAKE",
                Collections.singletonList("§7%server_online_players%§8/§7%server_max_players%"),
                (short) 0)
            )).write(Paths.get(directory + "/config.json"));
        }

        if (!Files.exists(Paths.get(databaseDir + "/database.json"))) {
            new Configuration()
                .addValue("database", new HashMap<>())
                .write(databaseDir + "/database.json");
        }

        this.mobs = Configuration.parse(databaseDir + "/database.json")
            .getValue("database", new TypeToken<Map<UUID, SelectorMob>>() {
            }.getType());
        this.selectorMobConfig = Configuration.parse(directory + "/config.json")
            .getValue("config", new TypeToken<SelectorMobConfig>() {
            }.getType());

        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
            new PacketOutEnableMobs()
        );
        ReformCloudLibraryService.sleep(500);
        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
            new PacketOutUpdateMobs(this.mobs, this.selectorMobConfig)
        );
    }

    private void saveDatabase() {
        new Configuration()
            .addValue("database", this.mobs == null ? new HashMap<>() : this.mobs)
            .write(databaseDir + "/database.json");
    }

    private void registerNetworkHandlers() {
        ReformCloudController.getInstance().getNettyHandler()
            .registerQueryHandler("RequestAll", new PacketInQueryGetAll());

        ReformCloudController.getInstance().getNettyHandler()
            .registerHandler("DeleteMob", new PacketInDeleteMob());
        ReformCloudController.getInstance().getNettyHandler()
            .registerHandler("CreateMob", new PacketInCreateMob());
    }

    public void close() {
        ReformCloudController.getInstance().getNettyHandler().unregisterQueryHandler("RequestAll");

        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("DeleteMob");
        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("CreateMob");

        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
            new PacketOutDisableMobs()
        );
        this.saveDatabase();
    }

    public void createMob(SelectorMob selectorMob) {
        Require.requireNotNull(selectorMob);
        this.mobs.put(selectorMob.getUniqueID(), selectorMob);
        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
            new PacketOutCreateMob(selectorMob)
        );
        this.saveDatabase();
    }

    public void deleteMob(UUID selectorMob) {
        Require.requireNotNull(selectorMob);
        SelectorMob selectorMob1 = this.mobs.remove(selectorMob);
        if (selectorMob1 != null) {
            ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
                ReformCloudController.getInstance().getInternalCloudNetwork()
                    .getServerProcessManager(),
                new PacketOutDeleteMob(selectorMob1)
            );
        }

        this.saveDatabase();
    }

    public SelectorMob getMob(UUID uuid) {
        return this.mobs.get(uuid);
    }

    public String getDirectory() {
        return this.directory;
    }

    public String getDatabaseDir() {
        return this.databaseDir;
    }

    public Map<UUID, SelectorMob> getMobs() {
        return this.mobs;
    }

    public SelectorMobConfig getSelectorMobConfig() {
        return this.selectorMobConfig;
    }
}
