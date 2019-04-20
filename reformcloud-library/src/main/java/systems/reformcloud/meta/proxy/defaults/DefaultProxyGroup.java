/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy.defaults;

import lombok.ToString;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@ToString
public final class DefaultProxyGroup extends ProxyGroup implements Serializable {
    private static final long serialVersionUID = -1867335836689571544L;

    public DefaultProxyGroup(int memory, final String client, ProxyVersions proxyVersions) {
        super(
                "Proxy",
                Collections.singletonList(client),
                new ArrayList<>(),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                ProxyModeType.DYNAMIC,
                true,
                true,
                false,
                25565,
                1,
                -1,
                512,
                memory,
                proxyVersions
        );
    }

    public DefaultProxyGroup(final String name, final String client, ProxyVersions proxyVersions) {
        super(
                name,
                Collections.singletonList(client),
                new ArrayList<>(),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                ProxyModeType.DYNAMIC,
                true,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                proxyVersions
        );
    }

    public DefaultProxyGroup(final String name, final String client, ProxyVersions proxyVersions, ProxyModeType proxyModeType) {
        super(
                name,
                Collections.singletonList(client),
                new ArrayList<>(),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                proxyModeType,
                true,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                proxyVersions
        );
    }
}
