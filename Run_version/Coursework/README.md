```text
your-project/
├── build.sbt
├── project/
│   ├── build.properties
│   └── plugins.sbt
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── gui/
│   │   │       ├── ChartIDE.java
│   │   │       ├── ChartFrame.java
│   │   │       └── ...
│   │   ├── scala/
│   │   │   └── engine/
│   │   │       ├── DSLParser.scala
│   │   │       ├── DrawingEngine.scala
│   │   │       ├── Commands.scala
│   │   │       └── MainApp.scala
│   │   └── resources/
│   │       └── (any *.conf or images you need)
│   └── test/
│       ├── java/
│       │   └── (if you have Java-based tests)
│       └── scala/
│           └── (if you have Scala-based tests)
└── target/
    └── (generated build artifacts)

```