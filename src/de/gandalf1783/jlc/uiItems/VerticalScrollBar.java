package de.gandalf1783.jlc.uiItems;

import de.gandalf1783.jlc.gfx.Assets;
import de.gandalf1783.jlc.main.Main;
import de.gandalf1783.jlc.threads.CLIUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

public class VerticalScrollBar extends ScrollItem {

    public double sliderPos = 0; // In Pixeln!
    public int sliderLength = 5; // Length in Elements, 1 element = 40 pixel width/height
    private final int x;
    private final int y;
    private boolean selected = false;
    private boolean inverted = false;
    private final boolean displayPercent;
    private final boolean isUniverseChannelSet;
    private int universe, channel;

    public VerticalScrollBar(int x, int y, Graphics g, Boolean inverted, Boolean displayPercent) {
        super(x, y, g);
        this.x = x;
        this.y = y;
        this.height = (sliderLength + 2) * 40;
        this.width = 40;
        this.bounds = new Rectangle(x, y, width, height);
        this.inverted = inverted;
        this.displayPercent = displayPercent;
        this.isUniverseChannelSet = false;
    }

    public VerticalScrollBar(int x, int y, Graphics g, Boolean inverted, Boolean displayPercent, int universe, int channel) {
        super(x, y, g);
        this.x = x;
        this.y = y;
        this.height = (sliderLength + 2) * 40;
        this.width = 40;
        this.bounds = new Rectangle(x, y, width, height);
        this.inverted = inverted;
        this.displayPercent = displayPercent;
        this.isUniverseChannelSet = true;
        this.universe = universe;
        this.channel = channel;
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
        if (displayPercent) {
            g.drawString(((int) getSliderPosPercent()) + "", x + 4, y + ((sliderLength * 40) / 2));
        }
    }

    @Override
    public void tick() {
        if (isUniverseChannelSet) {
            double percentage = this.getSliderPosPercent();
            double data = (percentage / 100) * 255;
            Main.setDmxByte((byte) data, universe, channel);
            byte[][] temp_dmxData = Main.getDmxData();
            Main.setDmxData(temp_dmxData);
        }
    }

    public int getMaxSliderPos() {
        return (sliderLength * 40) - 40;
    }

    public void getSliderPos() {
        CLIUtils.println("SliderPos: " + sliderPos);
    }

    public void setSliderLength(int sliderLength) {
        this.sliderLength = sliderLength;
    }

    public void setSliderPos(int sliderPos) {
        int pos = sliderPos;
        if (inverted) {
            pos = (sliderLength * 40) - pos;
        }
        this.sliderPos = pos;
        checkBounds();

    }

    public double getSliderPosPercent() {
        double pos = (sliderPos / ((sliderLength * 40) - 40) * 100);
        if (inverted) {
            pos = 100 - pos;
        }
        return pos;
    }

    public void setSliderPosPercent(double d) {
        if (inverted) {
            d = 100 - d;
        }
        double d1 = (d / 100) * getMaxSliderPos();
        if (inverted) {
            d1 = (sliderLength * 40) - d1;
        }
        setSliderPos((int) d1);
        checkBounds();
    }

    public void checkBounds() {
        if (sliderPos > getMaxSliderPos()) {
            sliderPos = getMaxSliderPos();
        }
        if (sliderPos < 0) {
            sliderPos = 0;
        }
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        if (!hovering)
            return;
        sliderPos = e.getY() - y - 60;
        checkBounds();
        selected = true;
        onClick(e);
    }

    @Override
    public void onMouseRelease(MouseEvent event) {
        this.selected = false;
    }


    @Override
    public void onMouseDrag(MouseEvent e) {
        if (!hovering)
            return;
        sliderPos = e.getY() - y - 60;
        checkBounds();
        selected = true;
    }

    public void refreshDMXValue(int finalU, int finalC) {
        double percentage = this.getSliderPosPercent();
        double data = (percentage / 100) * 255;
        Main.setDmxByte((byte) data, finalU, finalC);
        byte[][] temp_dmxData = Main.getDmxData();
        Main.setDmxData(temp_dmxData);
    }

    public void setInverted(Boolean b) {
        this.inverted = b;
    }
}
