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


  // reset pixels
  def clearPixels(): Unit = {
    _pixels = List.empty
  }
  
  // reset instructions
  def clearInstructions(): Unit = {
    _drawingInstructions = List.empty
  }
}