import javax.swing.*;
import java.awt.*;

public class GraphicPanel extends JPanel {
    private State state; // reference to state class

    public GraphicPanel(State state) {
        this.state = state;
    }
    
    @Override // called on initial render and repaint
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g); // default initialisation
        var g2 = (Graphics2D) g; // cast to advanced graphic resource, dont know if neccesary

        // should be iterating through state list, with cases for each command
        if (state.getState()) {
            g2.setColor(Color.BLUE);
            g2.drawOval(100, 100, 80, 80);  // Circle coordinates and size
        }
    }

    
}
