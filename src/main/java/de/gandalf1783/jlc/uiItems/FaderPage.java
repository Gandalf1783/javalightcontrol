package de.gandalf1783.jlc.uiItems;

import de.gandalf1783.jlc.main.Main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FaderPage extends UiItem {

    private ArrayList<VerticalScrollBar> faders = new ArrayList<>();
    private ArrayList<VerticalScrollBar> visibleFader = new ArrayList<>();
    private ArrayList<Button> navigation = new ArrayList<>();
    private int selectedFader = 0;
    private int selectedUni = 0;

    public FaderPage(int x, int y, Graphics g) {
        super(x, y, g);
        System.out.println("FADER 1 - " + selectedFader);
        int universes = Main.getSettings().getUniverseLimit() + 1;
        int channels = 512;
        System.out.println("FADER 2 - " + selectedFader);
        Button next = new Button(x + 110, y - 40, 40, 110, "Next ->", g) {
            @Override
            public void onChange() {
                System.out.println("PAGE +");
                selectedFader++;
                if (selectedFader > 31)
                    selectedFader = 31;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        System.out.println("FADER 3 - " + selectedFader);
        Button previous = new Button(x, y - 40, 40, 110, "<- Prev", g) {
            @Override
            public void onChange() {
                System.out.println("PAGE -");
                selectedFader--;
                if (selectedFader < 0)
                    selectedFader = 0;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        System.out.println("FADER 4 - " + selectedFader);
        Button universeUp = new Button(x + 330, y - 40, 40, 110, "Uni ↑", g) {
            @Override
            public void onChange() {
                System.out.println("UNI +");
                selectedUni++;
                if (selectedUni > 1)
                    selectedUni = 1;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        System.out.println("FADER 5 - " + selectedFader);
        Button universeDown = new Button(x + 220, y - 40, 40, 110, "Uni ↓", g) {
            @Override
            public void onChange() {
                System.out.println("UNI -");
                selectedUni--;
                if (selectedUni < 0)
                    selectedUni = 0;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        System.out.println("FADER 6 - " + selectedFader);
        int counter = 0;
        for (int u = 0; u < universes; u++) {
            for (int c = 0; c < channels; c++) {
                if (counter == 16)
                    counter = 0;
                int finalU = u;
                int finalC = c;
                VerticalScrollBar vsb = new VerticalScrollBar(x + (counter * 40) + (counter * 3), y, g, true, true) {
                    @Override
                    public void onChange(MouseEvent e) {
                        double percentage = this.getSliderPosPercent();
                        double data = (percentage / 100) * 255;
                        Main.setDmxByte((byte) data, finalU, finalC);
                        byte[][] temp_dmxData = Main.getDmxData();
                        Main.setDmxData(temp_dmxData);
                    }
                };
                vsb.setSliderPosPercent(0);
                faders.add(vsb);
                counter++;
            }
        }
        System.out.println("FADER 7 - " + selectedFader);
        navigation.add(previous);
        navigation.add(next);
        navigation.add(universeUp);
        navigation.add(universeDown);
        System.out.println("FADER 8 - " + selectedFader);
        setVisibleFader(0, 0);
        System.out.println("FADER 9 - " + selectedFader);
    }

    private void setVisibleFader(int i, int u) {
        ArrayList<VerticalScrollBar> temp = new ArrayList<>();
        for (int j = 0; j < 16; j++) {
            temp.add(faders.get(j + (i * 16) + (u * 512)));
        }
        visibleFader = temp;
    }

    @Override
    public void render(Graphics g) {
        for (VerticalScrollBar vsb : visibleFader) {
            vsb.render(g);
        }
        //        for (int i = 0; i < 16; i++) {
        //            faders.get((selectedFader*16)+i).render(g);
        //        }
        for (Button b : navigation) {
            b.render(g);
        }
        g.setColor(Color.BLUE);
        g.drawString("Universe: " + selectedUni, x + 440, y - 25);
        g.drawString("Page: " + selectedFader + "/" + 31, x + 440, y - 10);
        g.drawString("Faders " + (selectedFader * 16) + " - " + ((selectedFader * 16) + 16), x + 510, y - 25);
        g.setColor(Color.BLACK);
    }

    @Override
    public void tick() {
        for (VerticalScrollBar fader : faders)
            fader.tick();
    }

    @Override
    public void onMouseMove(MouseEvent e) {
        for (VerticalScrollBar fader : faders)
            fader.onMouseMove(e);
    }

    @Override
    public void onDrag(MouseEvent e) {
        for (VerticalScrollBar fader : faders)
            fader.onDrag(e);
    }

    @Override
    public void onClick(MouseEvent e) {
    }

    @Override
    public void onMouseRelease(MouseEvent e) {
        if (Main.getWindowThread().isOnCurrentPage(this)) {
            int count = 0;
            for (VerticalScrollBar fader : visibleFader) {
                if (fader.hovering) {
                    fader.onClick(e);
                }
            }
            for (Button b : navigation) {
                if (b.bounds.contains(e.getX(), e.getY())) {
                    b.onClick(e);
                }
            }
        }
    }
}
