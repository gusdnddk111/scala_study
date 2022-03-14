ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.6.18"

lazy val root = (project in file("."))
  .settings(
    name := "scala_exercise_2_advanced"
  )
