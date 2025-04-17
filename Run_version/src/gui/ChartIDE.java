// src/main/java/gui/ChartIDE.java
package gui;

import engine.Drawable;
import engine.ScalaBridge;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartIDE extends JFrame {

    private final DrawingPanel canvas = new DrawingPanel();
    private final JTextArea editor = new JTextArea(15, 40);
    private final JTextArea console = new JTextArea(10, 40);
    private final JButton drawButton = new JButton("Draw ▶");

    public ChartIDE() {
        super("Chart IDE — DSL + Scala Engine");

        console.setEditable(false);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane editorPane = new JScrollPane(editor);
        JScrollPane consolePane = new JScrollPane(console);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(editorPane, BorderLayout.NORTH);
        rightPanel.add(drawButton, BorderLayout.CENTER);
        rightPanel.add(consolePane, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvas, rightPanel);
        split.setResizeWeight(0.6);

        getContentPane().add(split);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        drawButton.addActionListener(e -> onDraw());
    }

    private void onDraw() {
        String script = editor.getText();
        try {
            console.setText("");
            List<Drawable> result = ScalaBridge.interpret(script);
            canvas.setScene(result);
            console.append("Rendered " + result.size() + " objects.\n");
        } catch (Exception ex) {
            console.setText("Error:\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChartIDE().setVisible(true));
    }
}
