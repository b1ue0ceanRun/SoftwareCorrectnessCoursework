# To Run
1. Compile Scala: `scalac Parser.scala`   
2. Compile Java: `javac Main.java`       
3. Run Java: `java -cp . Main`   


# To Test
StateTest.scala-->	sbt "testOnly StateTest"
ParserTest.scala -->	sbt "testOnly ParserTest"
DrawerTest.scala	--> sbt "testOnly DrawerTest"
GraphicPanelTest.java -->	sbt "testOnly GraphicPanelTest"