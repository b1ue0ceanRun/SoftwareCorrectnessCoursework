object State {
    
  private var _drawingInstructions: List[Command] = List.empty

  def getState: List[Command] = _drawingInstructions

  def addInstruction(instruction: Command): Unit = {
    _drawingInstructions = _drawingInstructions :+ instruction
  }

}
  