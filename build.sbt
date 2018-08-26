
name := """io_log_ingestion_manager"""
organization := "com.vision40"

version := "1.0-SNAPSHOT"

lazy val UnitTestConf = config("unit") extend Test
lazy val IntegrationTestConf = config("integration") extend Test

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies ++=Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test,
  "com.rabbitmq" % "amqp-client" % "5.3.0",
  "org.reactivemongo" %% "reactivemongo" % "0.16.0",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.16.0-play25",
  "com.typesafe.play" %% "play-json-joda" % "2.6.0-RC1",
  "org.mockito" % "mockito-all" % "1.9.5" % Test,
  "io.cucumber" %% "cucumber-scala" % "2.0.1" % Test,
  "org.scalaj" %% "scalaj-http" % "2.4.0" % Test
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .configs(IntegrationTestConf, UnitTestConf)
  .enablePlugins(PlayScala, CucumberPlugin)
  .settings(inConfig(UnitTestConf)(Defaults.testTasks): _*)
  .settings(inConfig(IntegrationTestConf)(Defaults.testTasks): _*)

CucumberPlugin.glue := "functional"

coverageExcludedPackages := "<empty>;Module;.*Reverse*Controller*;router.*"

lazy val unit = TaskKey[Unit]("unit", "Runs all Unit Tests.")
lazy val integration = TaskKey[Unit]("integration", "Runs all Integration Tests.")

unit := (test in UnitTestConf).value
integration := (test in IntegrationTestConf).value

testOptions in UnitTestConf := Seq(Tests.Filter(testPackageName => testPackageName.startsWith("unit")))
javaOptions in UnitTestConf += s"-Dconfig.file=${baseDirectory.value}/conf/application-testlocal.conf"

testOptions in IntegrationTestConf := Seq(Tests.Filter(testPackageName => testPackageName.startsWith("integration")))
javaOptions in IntegrationTestConf += s"-Dconfig.file=${baseDirectory.value}/conf/application-integrationtests.conf"