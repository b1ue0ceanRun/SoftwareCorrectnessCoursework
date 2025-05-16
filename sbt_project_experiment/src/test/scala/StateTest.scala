import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class StateTest extends AnyFunSuite with Matchers {

  test("Initial state is empty") {
    State.clearPixels()
    State.clearInstructions()
    State.getState shouldBe empty
    State.getPixels shouldBe empty
  }

  test("addInstruction adds a command to state") {
    State.clearInstructions()
    val command = Circle(10, 20, 30)
    State.addInstruction(command)
    State.getState shouldBe List(command)
  }

  test("addPixel adds pixel coordinates") {
    State.clearPixels()
    State.addPixel(1, 2)
    State.addPixel(3, 4)
    State.getPixels shouldBe List((1, 2), (3, 4))
  }

  test("clearPixels clears the pixel list") {
    State.addPixel(5, 6)
    State.clearPixels()
    State.getPixels shouldBe empty
  }

  test("clearInstructions clears the command list") {
    State.addInstruction(Rectangle(0, 0, 10, 10))
    State.clearInstructions()
    State.getState shouldBe empty
  }

  test("addText does not throw exception") {
    noException should be thrownBy {
      State.addText("test text", 50, 60)
    }
  }
}
