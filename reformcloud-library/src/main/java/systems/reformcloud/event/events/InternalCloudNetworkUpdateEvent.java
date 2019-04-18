/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

@AllArgsConstructor
@Getter
public final class InternalCloudNetworkUpdateEvent extends Event implements Serializable {
    private InternalCloudNetwork internalCloudNetwork;
}
