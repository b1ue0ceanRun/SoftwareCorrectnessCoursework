class State {
    
    private var _drawingInstructions: List[List[Any]] = List.empty

    def getState: List[List[Any]] = _drawingInstructions

    def updateState(newState: List[List[Any]]): Unit = {
    _drawingInstructions = newState
    }

    def addInstruction(instruction: List[Any]): Unit = {
      _drawingInstructions = _drawingInstructions :+ instruction
    }

}
