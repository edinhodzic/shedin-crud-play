import sbt._

organization := "io.shedin.library"

name := "shedin-crud-play"

version := "0.2.0-SNAPSHOT"

lazy val shedinCrudPlay = project.in(file("."))
  .settings(name := "shedin-crud-play")
  .settings(scalaVersion := "2.12.2")
  .settings(libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-library" % "2.12.2",
    "com.typesafe.play" % "play_2.12" % "2.6.0-M5",
    "io.shedin.library" % "shedin-crud-lib_2.12" % "0.2.0-SNAPSHOT",
    "org.scalatestplus.play" % "scalatestplus-play_2.12" % "3.0.0-RC1" % Test,
    "org.mockito" % "mockito-all" % "1.10.19" % Test))
  .settings(scalacOptions ++= Seq("-deprecation", "-feature"))
  .settings(ivyScala := ivyScala.value map {
    _.copy(overrideScalaVersion = true)
  })
