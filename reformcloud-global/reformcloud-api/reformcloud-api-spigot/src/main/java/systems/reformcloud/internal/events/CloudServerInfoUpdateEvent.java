/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

@Getter
@AllArgsConstructor
public final class CloudServerInfoUpdateEvent extends Event implements Serializable {
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
