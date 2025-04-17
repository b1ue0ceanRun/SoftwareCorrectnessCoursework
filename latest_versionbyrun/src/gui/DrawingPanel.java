// src/main/java/gui/DrawingPanel.java
package gui;

import engine.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingPanel extends JPanel {

    private List<Drawable> scene = java.util.Collections.emptyList();
    private int highlightIndex = -1;

    public void setScene(List<Drawable> scene) {
        this.scene = scene;
        repaint();
    }

    public void setHighlightIndex(int index) {
        this.highlightIndex = index;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (scene == null || scene.isEmpty())
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int i = 0;
        for (Drawable d : scene) {
            boolean isHighlighted = (i == highlightIndex);

            if (d instanceof DPixel p) {
                g2.setColor(isHighlighted ? Color.MAGENTA : p.color());
                g2.fillRect(p.x(), p.y(), 1, 1);

            } else if (d instanceof DText t) {
                g2.setColor(isHighlighted ? Color.MAGENTA : t.color());
                g2.drawString(t.text(), t.x(), t.y());
            }

            i++;
        }
    }
}
