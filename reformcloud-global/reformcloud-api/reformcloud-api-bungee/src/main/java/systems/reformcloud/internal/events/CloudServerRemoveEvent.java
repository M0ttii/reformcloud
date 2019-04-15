/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * This class represents the cloud server remove event
 * in the whole cloud
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 * @since RCS1.0
 */

@AllArgsConstructor
@Getter
public final class CloudServerRemoveEvent extends Event implements Serializable {
    private ServerInfo serverInfo;
}
