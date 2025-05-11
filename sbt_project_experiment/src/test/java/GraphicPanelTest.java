import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphicPanelTest {

    private GraphicPanel panel;

    @BeforeEach
    public void setup() {
        panel = new GraphicPanel();
    }

    @Test
    public void testAddPixelIncreasesSize() {
        panel.addPixel(10, 20);
        panel.addPixel(30, 40);

        // Using reflection to access private field for testing purposes
        List<Point> pixels = TestUtils.getPrivateField(panel, "pixels");
        assertEquals(2, pixels.size());
        assertEquals(new Point(10, 20), pixels.get(0));
        assertEquals(new Point(30, 40), pixels.get(1));
    }

    @Test
    public void testClearPixelsEmptiesList() {
        panel.addPixel(5, 5);
        panel.clearPixels();

        List<Point> pixels = TestUtils.getPrivateField(panel, "pixels");
        assertTrue(pixels.isEmpty());
    }

    @Test
    public void testWriteTextAddsJLabel() {
        int initial = panel.getComponentCount();
        panel.writeText("Hello", 50, 50);
        assertEquals(initial + 1, panel.getComponentCount());

        Component comp = panel.getComponent(panel.getComponentCount() - 1);
        assertTrue(comp instanceof JLabel);
        assertEquals("Hello", ((JLabel) comp).getText());
    }

    @Test
    public void testGetColorJavaDelegatesToDrawer() {
        Drawer.changeColor("RED");
        assertEquals(Color.RED, panel.getColorJava());
    }
}
