object State {
   
  private var _drawingInstructions: List[Command] = List.empty

  // pixel storage
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
}