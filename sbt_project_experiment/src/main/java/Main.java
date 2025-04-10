/* 
To Run:
- javac Main.java
- java Main  
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import scala.util.Either;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CHARTS!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        
        //scala classes
        State state = new State();
        Parser parser = new Parser(state);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // The two main sub-windows, graphics and code editor + the error window
        GraphicPanel graphicsView = new GraphicPanel(state);
        JTextArea codeEditor = new JTextArea();
        JPanel errorWindow = new JPanel();
        
        // make drawing button
        JButton button = new JButton("Render");
        button.setBackground(Color.PINK);
        button.setForeground(Color.DARK_GRAY);

        // (unmoveable) divider 
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphicsView, new JScrollPane(codeEditor));
        splitPane.setEnabled(false);
        splitPane.setDividerLocation(700);

        // the bottom panel
        JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, errorWindow, button);
        bottomSplitPane.setEnabled(false);
        bottomSplitPane.setDividerLocation(800);

        // add to main windows
        mainPanel.add(splitPane);
        bottomPanel.add(bottomSplitPane);
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        button.addActionListener(e -> {
            String code = codeEditor.getText();
            
            Either result = parser.receiveCode(code);
            
            if (result.isRight()) {
                graphicsView.update();
                // clear errorWindow
                System.out.println(result);
            } else {
                // show error in errorWindow
                String errorMsg = result.left().get().toString(); 
                System.out.println(result);
                JOptionPane.showMessageDialog(frame, errorMsg, "Parse Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}

