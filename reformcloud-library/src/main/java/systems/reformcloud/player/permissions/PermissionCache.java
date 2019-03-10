/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.player.permissions.group.PermissionGroup;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

@AllArgsConstructor
@Getter
public final class PermissionCache implements Serializable {
    private List<PermissionGroup> allRegisteredGroups;

    @Setter
    private PermissionGroup defaultGroup;

    public PermissionGroup getPermissionGroup(String name) {
        return this.allRegisteredGroups.stream().filter(e -> e.getName().startsWith(name)).findFirst().orElse(null);
    }
}
