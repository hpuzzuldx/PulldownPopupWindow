package com.ldx.pulldown.popupwindow;

public class MenuItem {
    public int itemId;
    public String itemTitle;

    public MenuItem(int itemId, String itemTitle) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }
}
