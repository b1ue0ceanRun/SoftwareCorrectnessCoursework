ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "JavaScalaInterop",
    Compile / compile / run / fork := true
    //Compile / mainClass := Some("MakeWindow")
  )

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0"