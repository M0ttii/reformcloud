/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.handlers;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

import systems.reformcloud.logging.LoggerProvider;

/**
 * Register a Handler in {@link LoggerProvider} to get the console input
 */
public interface IConsoleInputHandler {
    void handle(String message);
}
