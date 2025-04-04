import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

object ShapesForGraphics {
  def main(args: Array[String]): Unit = {

  }

  def my_drawing(): Graphics2D = {
    val size = (500, 500)
    val canvas = new BufferedImage(size._1, size._2, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setPaint(Color.PINK)
    g.fillRect(0, 0, 300, 400)
    g
  }

}
