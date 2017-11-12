package com.fa.grubot.objects.dashboard;

public class DashboardSettings extends DashboardItem {
    private int totalSettingsCount;

    public DashboardSettings(int totalSettingsCount) {
        this.totalSettingsCount = totalSettingsCount;
    }

    public int getTotalSettingsCount() {
        return totalSettingsCount;
    }
}
