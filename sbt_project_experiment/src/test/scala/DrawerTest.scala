import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.awt.Color

class MockGraphicPanel extends GraphicPanel {
  var cleared = false
  var updated = false
  val pixels = scala.collection.mutable.ListBuffer.empty[(Int, Int)]
  val texts = scala.collection.mutable.ListBuffer.empty[(String, Int, Int)]

  override def clearPixels(): Unit = cleared = true
  override def update(): Unit = updated = true
  override def addPixel(x: Int, y: Int): Unit = pixels += ((x, y))
  override def writeText(t: String, x: Int, y: Int): Unit = texts += ((t, x, y))
}

class DrawerTest extends AnyFunSuite with Matchers {

  def resetState(): Unit = {
    State.clearPixels()
    State.clearInstructions()
  }

  test("drawLine adds correct number of pixels") {
    resetState()
    Drawer.drawLine(0, 0, 3, 0)
    State.getPixels should contain allOf ((0, 0), (1, 0), (2, 0), (3, 0))
  }

  test("drawCircle plots symmetric pixels") {
    resetState()
    Drawer.drawCircle(0, 0, 1)
    val pixels = State.getPixels
    val expected = Set((0, 1), (0, -1), (1, 0), (-1, 0))
    expected.foreach { p =>
      assert(pixels.contains(p), s"Missing point $p in pixels: ${pixels.mkString(", ")}")
    }
  }

  test("changeColor sets correct color") {
    Drawer.changeColor("RED")
    Drawer.getColor() shouldBe Color.RED

    Drawer.changeColor("VIOLET")
    Drawer.getColor() shouldBe Color.MAGENTA

    Drawer.changeColor("UNKNOWN")
    Drawer.getColor() shouldBe Color.BLACK
  }

  test("drawFill calls shape-specific methods") {
    resetState()
    Drawer.drawFill("RED", Circle(0, 0, 1))
    Drawer.getColor() shouldBe Color.RED
    State.getPixels.nonEmpty shouldBe true
  }

  test("renderCommand dispatches based on type") {
    val panel = new MockGraphicPanel()
    Drawer.setGraphicPanel(panel)

    val commands = List(
      Circle(0, 0, 1),
      Line(0, 0, 1, 0),
      Rectangle(0, 0, 1, 1),
      TextAt(10, 10, "Hi"),
      BoundingBox(0, 0, 5, 5),
      Fill("GREEN", Rectangle(1, 1, 2, 2)),
      Draw("BLUE", List(Circle(0, 0, 1), Line(1, 1, 2, 2)))
    )

    resetState()
    commands.foreach(State.addInstruction)
    commands.foreach(Drawer.renderCommand)

    State.getPixels.nonEmpty shouldBe true
  }
}
