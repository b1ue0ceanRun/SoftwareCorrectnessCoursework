ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "JavaScalaInterop",
    Compile / compile / run / fork := true,
    Compile / mainClass := Some("Main"),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0",
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "org.junit.jupiter" % "junit-jupiter" % "5.10.2" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    testFrameworks += new TestFramework("org.junit.platform.sbt.plugin.JUnitPlatformFramework")
  )
