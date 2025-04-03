import java.awt.{Color, Graphics2D}
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage

object TextForGraphics {
  def main(args: Array[String]): Unit = {
    println(my_function())
  }

  def my_function(): String = {
    "Hello darkness my old friend!"
  }

  def my_drawing(): Graphics2D = {
    val size = (500, 500)
    val canvas = new BufferedImage(size._1, size._2, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setColor(Color.PINK)
    g.fill(new Ellipse2D.Double(30.0, 30.0, 80.0, 40.0))
    g.fill(new Ellipse2D.Double(230.0, 380.0, 40.0, 60.0))
    g
  }

}
