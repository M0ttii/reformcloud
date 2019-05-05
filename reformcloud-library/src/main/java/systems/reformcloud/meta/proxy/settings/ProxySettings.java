/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy.settings;

import systems.reformcloud.utility.map.maps.Double;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class ProxySettings implements Serializable {
    private String targetProxyGroup, tabHeader, tabFooter, maintenanceProtocol, protocol;
    private String[] playerInfo;
    private boolean protocolEnabled, tabEnabled, motdEnabled, slotCounter;
    private int moreSlots;
    private List<Double<String, String>> normalMotd, maintenanceMotd;

    @java.beans.ConstructorProperties({"targetProxyGroup", "tabHeader", "tabFooter", "maintenanceProtocol", "protocol", "playerInfo", "protocolEnabled", "tabEnabled", "motdEnabled", "slotCounter", "moreSlots", "normalMotd", "maintenanceMotd"})
    public ProxySettings(String targetProxyGroup, String tabHeader, String tabFooter, String maintenanceProtocol, String protocol, String[] playerInfo, boolean protocolEnabled, boolean tabEnabled, boolean motdEnabled, boolean slotCounter, int moreSlots, List<Double<String, String>> normalMotd, List<Double<String, String>> maintenanceMotd) {
        this.targetProxyGroup = targetProxyGroup;
        this.tabHeader = tabHeader;
        this.tabFooter = tabFooter;
        this.maintenanceProtocol = maintenanceProtocol;
        this.protocol = protocol;
        this.playerInfo = playerInfo;
        this.protocolEnabled = protocolEnabled;
        this.tabEnabled = tabEnabled;
        this.motdEnabled = motdEnabled;
        this.slotCounter = slotCounter;
        this.moreSlots = moreSlots;
        this.normalMotd = normalMotd;
        this.maintenanceMotd = maintenanceMotd;
    }

    public String getTargetProxyGroup() {
        return this.targetProxyGroup;
    }

    public String getTabHeader() {
        return this.tabHeader;
    }

    public String getTabFooter() {
        return this.tabFooter;
    }

    public String getMaintenanceProtocol() {
        return this.maintenanceProtocol;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String[] getPlayerInfo() {
        return this.playerInfo;
    }

    public boolean isProtocolEnabled() {
        return this.protocolEnabled;
    }

    public boolean isTabEnabled() {
        return this.tabEnabled;
    }

    public boolean isMotdEnabled() {
        return this.motdEnabled;
    }

    public boolean isSlotCounter() {
        return this.slotCounter;
    }

    public int getMoreSlots() {
        return this.moreSlots;
    }

    public List<Double<String, String>> getNormalMotd() {
        return this.normalMotd;
    }

    public List<Double<String, String>> getMaintenanceMotd() {
        return this.maintenanceMotd;
    }

    public void setTargetProxyGroup(String targetProxyGroup) {
        this.targetProxyGroup = targetProxyGroup;
    }

    public void setTabHeader(String tabHeader) {
        this.tabHeader = tabHeader;
    }

    public void setTabFooter(String tabFooter) {
        this.tabFooter = tabFooter;
    }

    public void setMaintenanceProtocol(String maintenanceProtocol) {
        this.maintenanceProtocol = maintenanceProtocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setPlayerInfo(String[] playerInfo) {
        this.playerInfo = playerInfo;
    }

    public void setProtocolEnabled(boolean protocolEnabled) {
        this.protocolEnabled = protocolEnabled;
    }

    public void setTabEnabled(boolean tabEnabled) {
        this.tabEnabled = tabEnabled;
    }

    public void setMotdEnabled(boolean motdEnabled) {
        this.motdEnabled = motdEnabled;
    }

    public void setSlotCounter(boolean slotCounter) {
        this.slotCounter = slotCounter;
    }

    public void setMoreSlots(int moreSlots) {
        this.moreSlots = moreSlots;
    }

    public void setNormalMotd(List<Double<String, String>> normalMotd) {
        this.normalMotd = normalMotd;
    }

    public void setMaintenanceMotd(List<Double<String, String>> maintenanceMotd) {
        this.maintenanceMotd = maintenanceMotd;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProxySettings)) return false;
        final ProxySettings other = (ProxySettings) o;
        final Object this$targetProxyGroup = this.getTargetProxyGroup();
        final Object other$targetProxyGroup = other.getTargetProxyGroup();
        if (this$targetProxyGroup == null ? other$targetProxyGroup != null : !this$targetProxyGroup.equals(other$targetProxyGroup))
            return false;
        final Object this$tabHeader = this.getTabHeader();
        final Object other$tabHeader = other.getTabHeader();
        if (this$tabHeader == null ? other$tabHeader != null : !this$tabHeader.equals(other$tabHeader)) return false;
        final Object this$tabFooter = this.getTabFooter();
        final Object other$tabFooter = other.getTabFooter();
        if (this$tabFooter == null ? other$tabFooter != null : !this$tabFooter.equals(other$tabFooter)) return false;
        final Object this$maintenanceProtocol = this.getMaintenanceProtocol();
        final Object other$maintenanceProtocol = other.getMaintenanceProtocol();
        if (this$maintenanceProtocol == null ? other$maintenanceProtocol != null : !this$maintenanceProtocol.equals(other$maintenanceProtocol))
            return false;
        final Object this$protocol = this.getProtocol();
        final Object other$protocol = other.getProtocol();
        if (this$protocol == null ? other$protocol != null : !this$protocol.equals(other$protocol)) return false;
        if (!java.util.Arrays.deepEquals(this.getPlayerInfo(), other.getPlayerInfo())) return false;
        if (this.isProtocolEnabled() != other.isProtocolEnabled()) return false;
        if (this.isTabEnabled() != other.isTabEnabled()) return false;
        if (this.isMotdEnabled() != other.isMotdEnabled()) return false;
        if (this.isSlotCounter() != other.isSlotCounter()) return false;
        if (this.getMoreSlots() != other.getMoreSlots()) return false;
        final Object this$normalMotd = this.getNormalMotd();
        final Object other$normalMotd = other.getNormalMotd();
        if (this$normalMotd == null ? other$normalMotd != null : !this$normalMotd.equals(other$normalMotd))
            return false;
        final Object this$maintenanceMotd = this.getMaintenanceMotd();
        final Object other$maintenanceMotd = other.getMaintenanceMotd();
        if (this$maintenanceMotd == null ? other$maintenanceMotd != null : !this$maintenanceMotd.equals(other$maintenanceMotd))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $targetProxyGroup = this.getTargetProxyGroup();
        result = result * PRIME + ($targetProxyGroup == null ? 43 : $targetProxyGroup.hashCode());
        final Object $tabHeader = this.getTabHeader();
        result = result * PRIME + ($tabHeader == null ? 43 : $tabHeader.hashCode());
        final Object $tabFooter = this.getTabFooter();
        result = result * PRIME + ($tabFooter == null ? 43 : $tabFooter.hashCode());
        final Object $maintenanceProtocol = this.getMaintenanceProtocol();
        result = result * PRIME + ($maintenanceProtocol == null ? 43 : $maintenanceProtocol.hashCode());
        final Object $protocol = this.getProtocol();
        result = result * PRIME + ($protocol == null ? 43 : $protocol.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getPlayerInfo());
        result = result * PRIME + (this.isProtocolEnabled() ? 79 : 97);
        result = result * PRIME + (this.isTabEnabled() ? 79 : 97);
        result = result * PRIME + (this.isMotdEnabled() ? 79 : 97);
        result = result * PRIME + (this.isSlotCounter() ? 79 : 97);
        result = result * PRIME + this.getMoreSlots();
        final Object $normalMotd = this.getNormalMotd();
        result = result * PRIME + ($normalMotd == null ? 43 : $normalMotd.hashCode());
        final Object $maintenanceMotd = this.getMaintenanceMotd();
        result = result * PRIME + ($maintenanceMotd == null ? 43 : $maintenanceMotd.hashCode());
        return result;
    }

    public String toString() {
        return "ProxySettings(targetProxyGroup=" + this.getTargetProxyGroup() + ", tabHeader=" + this.getTabHeader() + ", tabFooter=" + this.getTabFooter() + ", maintenanceProtocol=" + this.getMaintenanceProtocol() + ", protocol=" + this.getProtocol() + ", playerInfo=" + java.util.Arrays.deepToString(this.getPlayerInfo()) + ", protocolEnabled=" + this.isProtocolEnabled() + ", tabEnabled=" + this.isTabEnabled() + ", motdEnabled=" + this.isMotdEnabled() + ", slotCounter=" + this.isSlotCounter() + ", moreSlots=" + this.getMoreSlots() + ", normalMotd=" + this.getNormalMotd() + ", maintenanceMotd=" + this.getMaintenanceMotd() + ")";
    }
}
