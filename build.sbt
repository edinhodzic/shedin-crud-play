import sbt._

organization := "io.shedin.library"

name := "shedin-crud-play"

version := "0.1.0-SNAPSHOT"

lazy val shedinCrudPlay = project.in(file("."))
  .settings(name := "shedin-crud-play")
  .settings(scalaVersion := "2.11.8")
  .settings(libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.11.8",
    "com.typesafe.play" % "play_2.11" % "2.5.10",
    "io.shedin.library" % "shedin-crud-lib_2.11" % "0.1.0-SNAPSHOT"))
  .settings(scalacOptions ++= Seq("-deprecation", "-feature"))
  .settings(ivyScala := ivyScala.value map {
    _.copy(overrideScalaVersion = true)
  })
