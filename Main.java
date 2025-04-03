// Main.java

/* 
To Run:
- javac Main.java
- java Main  
 */

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CHARTS!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // The two main sub-windows, graphics and code editor
        JPanel graphicsView = new JPanel();
        JTextArea codeEditor = new JTextArea();        
        
        // make drawing button
        JButton button = new JButton("Render");
    
        // (unmoveable) divider 
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphicsView, new JScrollPane(codeEditor));
        splitPane.setEnabled(false);
        splitPane.setDividerLocation(400);
        
        // add to main window
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(button, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}