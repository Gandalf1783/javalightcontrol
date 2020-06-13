package de.gandalf1783.jlc.uiItems;

import java.awt.*;
import java.util.ArrayList;

public class UiPage extends UiNavigator {

    private ArrayList<UiItem> uiItems = new ArrayList<>();
    private String pageName = "Page";

    public UiPage(int x, int y, Graphics g) {
        super(x, y, g);
    }

    public void addUiItem(UiItem item) {
        uiItems.add(item);
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String s) {
        pageName = s;
    }
}
