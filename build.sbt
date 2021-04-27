import sbt.Keys.libraryDependencies

val scala3Version = "3.0.0-RC3"
val zioVersion: String = "1.0.7"

lazy val root = project
  .in(file("."))
  .settings(
    name := "kanban-metrics",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "dev.zio" %% "zio" % zioVersion,
    libraryDependencies += "dev.zio" %% "zio-streams" % zioVersion
  )
