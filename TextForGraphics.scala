import java.awt.{Color, Graphics2D, Panel}
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

object TextForGraphics {
  def main(args: Array[String]): Unit = {
    println(commandsText())
  }

  def commandsText(): String = {
    "Hello darkness my old friend!"
  }

  def labelsForGraphics(): String = {
    "This is label"
  }


}
