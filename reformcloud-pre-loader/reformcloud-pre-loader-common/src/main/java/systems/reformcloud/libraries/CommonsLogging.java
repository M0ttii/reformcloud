/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class CommonsLogging extends Dependency implements Serializable {

    public CommonsLogging() {
        super(null, "1.2");
    }

    @Override
    public String getGroupID() {
        return "commons-logging";
    }

    @Override
    public String getName() {
        return "commons-logging";
    }
}
