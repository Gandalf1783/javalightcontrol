package de.gandalf1783.jlc.uiItems;

import de.gandalf1783.jlc.main.Main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FaderPage extends UiItem {

    private final ArrayList<VerticalScrollBar> faders = new ArrayList<>();
    private ArrayList<VerticalScrollBar> visibleFader = new ArrayList<>();
    private final ArrayList<Button> navigation = new ArrayList<>();
    private int selectedFader = 0;
    private int selectedUni = 0;

    public FaderPage(int x, int y, Graphics g) {
        super(x, y, g);
        int universes = Main.getProject().getUniverseLimit() + 1;
        int channels = 512;
        Button next = new Button(x + 110, y - 40, 40, 110, "Next ->", g) {
            @Override
            public void onClick(MouseEvent e) {
                selectedFader++;
                if (selectedFader > 31)
                    selectedFader = 31;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        Button previous = new Button(x, y - 40, 40, 110, "<- Prev", g) {
            @Override
            public void onClick(MouseEvent e) {
                selectedFader--;
                if (selectedFader < 0)
                    selectedFader = 0;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        Button universeUp = new Button(x + 330, y - 40, 40, 110, "Uni ↑", g) {
            @Override
            public void onClick(MouseEvent e) {
                selectedUni++;
                if (!(selectedUni < Main.getProject().getUniverseLimit()))
                    selectedUni = Main.getProject().getUniverseLimit() - 1;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        Button universeDown = new Button(x + 220, y - 40, 40, 110, "Uni ↓", g) {
            @Override
            public void onClick(MouseEvent e) {
                selectedUni--;
                if (selectedUni < 0)
                    selectedUni = 0;
                setVisibleFader(selectedFader, selectedUni);
            }
        };
        int counter = 0;
        for (int u = 0; u < universes; u++) {
            for (int c = 0; c < channels; c++) {
                if (counter == 16)
                    counter = 0;
                VerticalScrollBar vsb = new VerticalScrollBar(x + (counter * 40) + (counter * 3), y, g, true, true, u, c);
                if (u < Main.getProject().getUniverseLimit()) {
                    if (Main.getDmxData() != null) {
                        byte i = Main.getDmxData()[u][c];
                        int j = i & 0xFF;
                        vsb.setSliderPosPercent((double) (j * 100) / 255);
                    }
                }
                faders.add(vsb);
                counter++;
            }
        }
        navigation.add(previous);
        navigation.add(next);
        navigation.add(universeUp);
        navigation.add(universeDown);
        setVisibleFader(0, 0);
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
        g.drawString("Faders " + ((selectedFader * 16) + 1) + " - " + ((selectedFader * 16) + 16), x + 510, y - 25);
        g.setColor(Color.BLACK);
    }

    @Override
    public void tick() {
        for (VerticalScrollBar fader : faders)
            fader.tick();
    }

    @Override
    public void onMouseMove(MouseEvent e) {
        for (VerticalScrollBar fader : visibleFader) {
            fader.onMouseMove(e);
        }
        for (Button b : navigation) {
            b.onMouseMove(e);
        }
    }

    @Override
    public void onClick(MouseEvent e) {
    }

    @Override
    public void onMouseRelease(MouseEvent e) {
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        for (VerticalScrollBar fader : visibleFader) {
            if (fader.hovering) {
                fader.onMouseClicked(e);
            }
        }
        for (Button b : navigation) {
            if (b.hovering) {
                b.onMouseClicked(e);
            }
        }
    }

    @Override
    public void onMouseDrag(MouseEvent e) {
        for (VerticalScrollBar fader : visibleFader) {
            if (fader.hovering) {
                fader.onMouseDrag(e);
                fader.tick();
            }
        }
    }
}
