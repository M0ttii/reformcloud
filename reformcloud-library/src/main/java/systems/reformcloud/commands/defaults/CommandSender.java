/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.defaults;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.logging.AbstractLoggerProvider;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public class CommandSender implements systems.reformcloud.commands.interfaces.CommandSender {
    /**
     * Sends a message to the console
     *
     * @param message       The message which should be sent
     */
    @Override
    public void sendMessage(String message) {
        AbstractLoggerProvider.defaultLogger().info().accept(message);
    }

    /**
     * Returns always {@code true} because the console has all permissions
     *
     * @param permission        The permission which should be checked
     * @return {@code true}
     */
    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
