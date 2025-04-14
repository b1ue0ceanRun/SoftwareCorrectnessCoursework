package sbt_project_experiment.src.main.java
class State {
    
    private var _drawingInstructions = List.empty

    def getState: List[List[Any]] = _drawingInstructions

    def addInstruction(instruction: List[Any]): Unit =
    {
      //_drawingInstructions = _drawingInstructions :+ instruction
    }

}
