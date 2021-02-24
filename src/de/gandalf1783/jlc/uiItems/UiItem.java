package de.gandalf1783.jlc.uiItems;

import java.awt.*;
import java.awt.event.MouseEvent;

public class UiItem {

    // Static Stuff here
    protected Rectangle bounds;
    protected boolean hovering = false;
    protected int x, y;
    protected int width, height;
    // Class here

    public UiItem(int x, int y, Graphics g) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        this.render(g);
    }

    public void tick() {
        this.tick();
    }

    public void onClick(MouseEvent e) {
    }

    public void onMouseMove(MouseEvent e) {
            hovering = bounds.contains(e.getX(), e.getY());
    }

    public void onMouseRelease(MouseEvent e) {
    }

    public void onMouseClicked(MouseEvent e) {
        if (hovering)
            onClick(e);
    }

    public void onMouseDrag(MouseEvent e) {
        if (!hovering)
            return;
    }
}
