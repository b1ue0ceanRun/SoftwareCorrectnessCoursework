import javax.swing.*;
import javax.swing.text.Utilities;
import java.awt.*;
import scala.util.Either;



public class Main {
    // Global constants for frame size
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 700;

    public static void main(String[] args) {
        
        
        JFrame frame = new JFrame("CHARTS!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
                
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // The two main sub-windows, graphics and code editor + the error window
        GraphicPanel graphicsView = new GraphicPanel();
        Drawer$.MODULE$.setGraphicPanel(graphicsView);
        JTextArea codeEditor = new JTextArea();
        JTextArea errorWindow = new JTextArea();

        // ErroeWindow style
        errorWindow.setBackground(Color.LIGHT_GRAY);
        errorWindow.setFont(errorWindow.getFont().deriveFont(Font.BOLD, 14f));
        
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
            
            // clear state and UI for new parsing
            State$.MODULE$.clearInstructions(); // clear instructions first
            State$.MODULE$.clearPixels();       // clear pixels
            errorWindow.setText("");            // clear error window
            errorWindow.setBackground(Color.LIGHT_GRAY);
            
            Either result = Parser$.MODULE$.receiveCode(code);
            
            if (result.isRight()) {
                Drawer$.MODULE$.drawSequence();
            } else {
                String errorMsg = result.left().get().toString();
                errorWindow.setBackground(Color.RED);
                errorWindow.setText(errorMsg);
            }
        });

    }
}

