/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

@Getter
public final class DiscordConfig implements Serializable {
    private DiscordInformations discordInformations;

    public DiscordConfig() {
        if (!Files.exists(Paths.get("reformcloud/addons/discord/config.json"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/discord"));
            new Configuration()
                    .addProperty("config", new DiscordInformations(
                            "NTQ0MTIyMTMwMzEzMjQ4NzY5.D0Ggvg.eMhB10edmYVmYo1-zg_u2nUNsD0",
                            "535909711178891279",
                            "ReformCloud - The official CloudSystem"
                    )).write(Paths.get("reformcloud/addons/discord/config.json"));
        }

        this.discordInformations = Configuration.parse(Paths.get("reformcloud/addons/discord/config.json"))
                .getValue("config", new TypeToken<DiscordInformations>() {
                }.getType());
    }
}
