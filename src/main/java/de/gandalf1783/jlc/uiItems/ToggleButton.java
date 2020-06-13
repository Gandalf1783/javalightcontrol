package de.gandalf1783.jlc.uiItems;

import de.gandalf1783.jlc.gfx.Assets;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ToggleButton extends Button {

    public Boolean toggled = false;

    public ToggleButton(int x, int y, int height, int width, String text, Graphics g) {
        super(x, y, height, width, text, g);
    }

    @Override
    public void render(Graphics g) {
        if (hovering) {
            g.drawImage(Assets.btn_hover, x, y, width, height, null);
        } else if (toggled) {
            g.drawImage(Assets.btn_press, x, y, width, height, null);
        } else {
            g.drawImage(Assets.btn, x, y, width, height, null);
        }
        g.drawString(text, x + 15, y + 25);
        g.drawRect(x, y, width, height);
    }

    @Override
    public void onClick(MouseEvent e) {
        toggled = !toggled;
    }

}
