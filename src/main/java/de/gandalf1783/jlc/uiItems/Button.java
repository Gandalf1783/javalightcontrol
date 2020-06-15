package de.gandalf1783.jlc.uiItems;

import de.gandalf1783.jlc.gfx.Assets;
import de.gandalf1783.jlc.main.Main;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button extends UiItem {

    public String text;
    private Boolean click = false;

    public Button(int x, int y, int height, int width, String text, Graphics g) {
        super(x, y, g);
        this.height = height;
        this.width = width;
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);
    }

    @Override
    public void render(Graphics g) {
        if (click) {
            g.drawImage(Assets.btn_press, x, y, width, height, null);
            click = false;
        } else if (hovering) {
            g.drawImage(Assets.btn_hover, x, y, width, height, null);
        } else {
            g.drawImage(Assets.btn, x, y, width, height, null);
        }

        g.drawString(this.text, x + 15, y + 25);
    }

    @Override
    public void onClick(MouseEvent e) {
        click = true;
        onChange();
    }

    @Override
    public void tick() {

    }

    @Override
    public void onMouseRelease(MouseEvent e) {
        if (hovering && Main.getWindowThread().isOnCurrentPage(this)) {
            click = true;
            this.onClick(e);
        }
    }

    public void onChange() {

    }
}
