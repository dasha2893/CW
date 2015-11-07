name := "CW"

version := "1.0"

lazy val `cw` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc ,
  cache ,
  ws ,
  "com.typesafe.play" % "anorm_2.11" % "2.4.0-M3",
  "postgresql" % "postgresql" % "9.1-901.jdbc4")


unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  