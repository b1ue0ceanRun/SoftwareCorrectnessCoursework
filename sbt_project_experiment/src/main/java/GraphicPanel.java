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
        
        // move origo to bottom left: -65 is the height of the error window
        g2.translate(0, (Main.FRAME_HEIGHT - 65)); //(LINE (0 0) (700 635)): the line goes from left bottom to right corner
                                                    // (LINE (700 0) (0 635)): the line going from left top to right bottom
        // (LINE (350 0) (350 700)): vertical line going through center
        // (LINE (0 317) (700 317)): horizontal line going through center

        // flip y-axis so positive is up
        g2.scale(1, -1);

        //writeText("Hello Seal", 400, 100);
        // draw all pixels
        g2.setColor(Color.BLACK);

        for (Point p : pixels) {
            g2.fillRect((p.x), (p.y), 1, 1); // 1px dot
        }
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
    // Drawing:
    // (LINE (0 0) (700 635)):
    //(LINE (700 0) (0 635)):
    //(LINE (350 0) (350 700)):
    //(LINE (0 317) (700 317)):
    //(CIRCLE (350 317) 50):
    //(CIRCLE (350 317) 100):
    //(CIRCLE (350 317) 150):
    //(LINE (280 700) (700 317)):
    //(LINE (700 317) (350 0)):
    //(LINE (350 0) (0 317)):
    //(LINE (0 317) (420 700)):
    //(RECTANGLE (200 140) (500 500)):

}