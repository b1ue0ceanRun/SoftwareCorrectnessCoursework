class State {
    private var _hasCircle = false

    def getState: Boolean = _hasCircle

    def updateState(newState: Boolean): Unit = {
    _hasCircle = newState
  }

}
