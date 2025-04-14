object State {
   
  private var _drawingInstructions: List[Command] = List.empty
  
  private var _pixels: List[(Int, Int)] = List.empty
  
  def getState: List[Command] = _drawingInstructions
  
  def getPixels: List[(Int, Int)] = _pixels
  
  def addInstruction(instruction: Command): Unit = {
    _drawingInstructions = _drawingInstructions :+ instruction
  }
  
  def addPixel(x: Int, y: Int): Unit = {
    _pixels = _pixels :+ (x, y)
  }
  
  def clearPixels(): Unit = {
    _pixels = List.empty
  }
  
  // reset all state - both instructions and pixels
  def clearAll(): Unit = {
    _drawingInstructions = List.empty
    _pixels = List.empty
  }
}