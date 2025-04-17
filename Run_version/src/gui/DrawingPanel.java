// src/main/java/gui/DrawingPanel.java
package gui;

import engine.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingPanel extends JPanel {

    private List<Drawable> scene = java.util.Collections.emptyList();
    private int highlightIndex = -1;

    public void setHighlightIndex(int index) {
        this.highlightIndex = index;
        repaint();
    }


    public void setScene(List<Drawable> scene) {
        this.scene = scene;
        repaint();
    }

    private Color getColor(Drawable d) {
        if (d instanceof DLine l)   return l.color();
        if (d instanceof DRect r)   return r.color();
        if (d instanceof DCircle c) return c.color();
        if (d instanceof DText t)   return t.color();
        return Color.BLACK; // fallback
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scene == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int i = 0;
        for (Drawable d : scene) {
            boolean isHighlighted = (i == highlightIndex);
            Color col = getColor(d);
            g2.setColor(isHighlighted ? Color.MAGENTA : col);

            if (d instanceof DLine l) {
                g2.drawLine(l.x1(), l.y1(), l.x2(), l.y2());
            } else if (d instanceof DRect r) {
                g2.fillRect(r.x1(), r.y1(), r.x2() - r.x1(), r.y2() - r.y1());
            } else if (d instanceof DCircle c) {
                g2.fillOval(c.cx() - c.r(), c.cy() - c.r(), 2 * c.r(), 2 * c.r());
            } else if (d instanceof DText t) {
                g2.drawString(t.text(), t.x(), t.y());
            }

            i++;
        }

    }

}
