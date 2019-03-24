/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * This class represents the cloud server remove event
 * in the whole cloud
 *
 * @since RCS1.0
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

@AllArgsConstructor
@Getter
public class CloudServerRemoveEvent extends Event {
    private ServerInfo serverInfo;
}
