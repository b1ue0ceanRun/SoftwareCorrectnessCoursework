import javax.swing.*;
import java.awt.*;

public class GraphicPanel extends JPanel {

    public GraphicPanel() {
    }
    @Override // called on initial render and repaint
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g); // default initialisation
        var g2 = (Graphics2D) g; // cast to advanced graphic resource
        
        
    }

    public void update() {
        this.repaint();
    }




    
}
