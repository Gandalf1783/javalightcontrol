package de.gandalf1783.jlc.uiItems;

import de.gandalf1783.jlc.gfx.Assets;

import java.awt.*;
import java.awt.event.MouseEvent;

public class VerticalScrollBar extends ScrollItem {

    private double sliderPos = 0; // In Pixeln!
    private int sliderLength = 5; // Length in Elements, 1 element = 40 pixel width/height
    private int x, y;
    private boolean selected = false;

    public VerticalScrollBar(int x, int y, Graphics g) {
        super(x, y, g);
        this.x = x;
        this.y = y;
        this.height = (sliderLength + 2) * 40;
        this.width = 40;
        this.bounds = new Rectangle(x, y, width, height);
    }


    @Override
    public void render(Graphics g) {
        checkBounds();
        int renderedLength = 1;
        g.drawImage(Assets.v_scrollbar[0], x, y, 40, 40, null);

        while (!(renderedLength > sliderLength)) {
            g.drawImage(Assets.v_scrollbar[2], x, (renderedLength * 40) + y, 40, 40, null);
            renderedLength++;
        }
        int length_offset = 0;
        length_offset = 40 + (sliderLength * 40);
        g.drawImage(Assets.v_scrollbar[3], x, length_offset + y, 40, 40, null);
        g.drawImage(Assets.v_scrollbar[1], x, (int) sliderPos + y + 40, 40, 40, null);
    }

    @Override
    public void tick() {
    }

    public int getMaxSliderPos() {
        return (sliderLength * 40) - 40;
    }

    public void getSliderPos() {
        System.out.println("SliderPos: " + sliderPos);
    }

    public void setSliderLength(int sliderLength) {
        this.sliderLength = sliderLength;
    }

    public void setSliderPos(int sliderPos) {
        this.sliderPos = sliderPos;
    }

    public double getSliderPosPercent() {
        double pos = sliderPos / sliderLength;
        return pos;
    }

    public void setSliderPosPercent(double d) {
        double d1 = (d / 100) * getMaxSliderPos();
        setSliderPos((int) d1);
    }

    private void checkBounds() {
        if (sliderPos > getMaxSliderPos()) {
            sliderPos = getMaxSliderPos();
        }
        if (sliderPos < 0) {
            sliderPos = 0;
        }
    }

    @Override
    public void onClick(MouseEvent e) {
        sliderPos = e.getY() - y - 60;
        selected = true;
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        selected = false;
    }

}
