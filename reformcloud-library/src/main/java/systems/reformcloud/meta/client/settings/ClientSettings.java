/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.client.settings;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

public enum ClientSettings implements Serializable {
    START_HOST("general.start-host"),
    MEMORY("general.memory"),
    MAX_CPU_USAGE("general.maxcpuusage"),
    MAX_LOG_SIZE("general.max-log-size");

    /**
     * The config string of the client setting
     */
    private String configString;

    /**
     * Creates a new client setting
     *
     * @param configString      The config string of the setting
     */
    ClientSettings(String configString) {
        this.configString = configString;
    }

    /**
     * Get a setting by the name
     *
     * @param in    The name of the setting
     * @return The setting found by the name
     */
    public static ClientSettings getSettingByName(String in) {
        ClientSettings clientSettings;
        try {
            clientSettings = valueOf(in.toUpperCase());
        } catch (final Throwable throwable) {
            return null;
        }

        return clientSettings;
    }

    public String getConfigString() {
        return this.configString;
    }
}
