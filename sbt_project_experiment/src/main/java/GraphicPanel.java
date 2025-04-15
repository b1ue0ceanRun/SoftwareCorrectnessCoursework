import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicPanel extends JPanel {
    // store pixels to draw
    private List<Point> pixels = new ArrayList<>();
    
    public GraphicPanel() {
        setBackground(Color.WHITE); 
    }
    
    @Override // called on initial render and repaint
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // default initialisation
        var g2 = (Graphics2D) g; // cast to advanced graphic resource
        
        // move origo to bottom left
        g2.translate(0, Main.FRAME_HEIGHT);

        // flip y-axis so positive is up
        g2.scale(1, -1);


        // draw all pixels
        g2.setColor(Color.BLACK);
        for (Point p : pixels) {
            g2.fillRect((p.x), (p.y), 1, 1); // 1px dot
        }
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