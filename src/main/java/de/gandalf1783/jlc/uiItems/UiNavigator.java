package de.gandalf1783.jlc.uiItems;

import java.awt.*;
import java.util.ArrayList;

public class UiNavigator extends UiItem {

    private ArrayList<UiPage> pages = new ArrayList<>();
    private int currentPage = 0;

    public UiNavigator(int x, int y, Graphics g) {
        super(x, y, g);
    }

    public void addPage(UiPage page) {
        pages.add(page);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int i) {
        currentPage = i;
    }

    public int getPageCount() {
        return pages.size();
    }
}
