package com.mehdiii.duelgame.models;

/**
 * Created by mehdiii on 12/24/15.
 */
public class DrawerItem {
    String title;
    String icon;

    public DrawerItem(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
