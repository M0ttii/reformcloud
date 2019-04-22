/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.network.in.PacketInRequestProperties;
import systems.reformcloud.properties.PropertiesGroup;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@Getter
public final class PropertiesConfig implements Serializable {
    @Getter
    private static PropertiesConfig instance;

    private systems.reformcloud.properties.PropertiesConfig propertiesConfig;

    public PropertiesConfig() {
        if (instance != null)
            throw new InstanceAlreadyExistsException();

        instance = this;

        if (!Files.exists(Paths.get("reformcloud/addons/properties"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/properties"));
            new Configuration()
                    .addProperty("config", new systems.reformcloud.properties.PropertiesConfig(
                            Collections.singletonList(new PropertiesGroup(
                                    "Lobby",
                                    new Properties()
                            ))
                    )).write(Paths.get("reformcloud/addons/properties/config.json"));
        }

        this.propertiesConfig = Configuration.parse("reformcloud/addons/properties/config.json")
                .getValue("config", new TypeToken<systems.reformcloud.properties.PropertiesConfig>() {
                });
        this.registerNetworkHandlers();
    }

    private void registerNetworkHandlers() {
        ReformCloudController.getInstance().getNettyHandler().registerQueryHandler("RequestProperties", new PacketInRequestProperties());
    }
}
