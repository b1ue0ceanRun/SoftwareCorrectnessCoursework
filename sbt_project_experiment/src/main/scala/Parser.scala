
class Parser(state: State) {
    def receiveCode(code: String): Unit = {      
      code match {
        case "circle" => state.updateState(true)
        case _ => println(s"ERROR / Not implemented")
    }
    }
}


