/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

@AllArgsConstructor
@Getter
public class CloudServerAddEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    private ServerInfo serverInfo;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
