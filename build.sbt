organization in ThisBuild := "io.kanaka"



version in ThisBuild := "0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

lazy val root = project.in(file("."))
  .disablePlugins(JmhPlugin)
  .settings(
      name := "galvanic"
  )

lazy val benchmarks = project.in(file("benchmarks"))
  .enablePlugins(JmhPlugin)
  .dependsOn(root)


