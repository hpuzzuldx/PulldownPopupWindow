package com.ldx.pulldown.nightmode;

/**
 * Created by lidongxiu on 2018/7/9.
 */

public class DemoBean {
    public int itemId;
    public String itemTitle;

    public DemoBean(int itemId, String itemTitle) {
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
