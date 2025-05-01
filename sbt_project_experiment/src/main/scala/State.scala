import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.{JLabel, JPanel}

object State {
   
  private var _drawingInstructions: List[Command] = List.empty
  private var _pixels: List[(Int, Int)] = List.empty
  
  def getState: List[Command] = _drawingInstructions
  def getPixels: List[(Int, Int)] = _pixels
  
  def addInstruction(instruction: Command): Unit = {
    _drawingInstructions = _drawingInstructions :+ instruction
  }
  
  // add new pixel
  def addPixel(x: Int, y: Int): Unit = {
    _pixels = _pixels :+ (x, y)
  }

  def addText(t: String, x: Int, y: Int): Unit = {
    val smallPanel = new JPanel()
    val label = new JLabel()
    smallPanel.setBounds(x, y, x*10, y*10)
    label.setText(t)
    label.setLocation(x, y)
    label.setForeground(Color.RED)
    smallPanel.add(label)
    smallPanel.add(label)
  }

  // reset pixels
  def clearPixels(): Unit = {
    _pixels = List.empty
  }
  
  // reset instructions
  def clearInstructions(): Unit = {
    _drawingInstructions = List.empty
  }
}