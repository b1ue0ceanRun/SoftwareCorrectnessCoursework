import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicPanel extends JPanel {
    // store pixels to draw
    private List<Point> pixels = new ArrayList<>();
    //private JLabel label = new JLabel();
    
    public GraphicPanel() {
        setBackground(Color.WHITE);
        setLayout(null);
        //add(label);
    }
    
    @Override // called on initial render and repaint
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // default initialisation
        var g2 = (Graphics2D) g; // cast to advanced graphic resource
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // move origo to bottom left: -65 is the height of the error window
        g2.translate(0, (Main.FRAME_HEIGHT - 65));

        // flip y-axis so positive is up
        g2.scale(1, -1);

        // draw all pixels
        //g2.setColor(Color.BLACK);
        g2.setColor(getColorJava());

        for (Point p : pixels) {
            g2.fillRect((p.x), (p.y), 1, 1); // 1px dot
        }
    }

    public Color getColorJava() {
        return Drawer.getColor();
    }


    public void writeText(String t, int x, int y) {
        JLabel label = new JLabel();
        label.setText(t);
        Dimension labelSize = label.getPreferredSize();
        label.setBounds(x, y, labelSize.width + 20, labelSize.height);
        label.setForeground(Color.BLUE);
        add(label);
        // the text is not erased after refreshing the window
    }

    
    // add a pixel to draw
    public void addPixel(int x, int y) {
        pixels.add(new Point(x, y));
    }


    public void clearPixels() {
        pixels.clear();
        repaint();
    }
    
    public void update() {
        this.repaint();
    }

}