name := "monitoring-akka-prometheus-kamon"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.9"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaVersion = "2.4.18"
  val akkaHttpVersion = "10.0.6"
  val kamonVersion = "0.6.3"
  val kamonPrometheusVersion = "0.2.0"
  val typesafeConfigVersion = "1.3.1"
  val logbackVersion = "1.1.5"
  val scalaTestVersion = "3.0.1"
  val junitVersion = "4.12"
  Seq(
    "com.typesafe" % "config" % typesafeConfigVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    // HTTP
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    // CLUSTER
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    // LOGGING
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    // MONITORING
    "io.kamon" %% "kamon-core" % kamonVersion,
    "io.kamon" %% "kamon-scala" % kamonVersion,
    "io.kamon" %% "kamon-akka" % kamonVersion,
    "io.kamon" %% "kamon-akka-remote_akka-2.4" % kamonVersion,
    "com.monsanto.arch" %% "kamon-prometheus" % kamonPrometheusVersion,
    // TESTING
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "junit" % "junit" % junitVersion % Test
  )
}

// AspectJ Weaver is required by Kamon Scala/Akka
aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignParameters, true)
  .setPreference(AlignArguments, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(DanglingCloseParenthesis, Preserve)
  .setPreference(RewriteArrowSymbols, true)

addCommandAlias("runSingle", "runMain com.michalplachta.shoesorter.api.SingleNodeApp")

addCommandAlias("runSharded", "runMain com.michalplachta.shoesorter.api.ShardedApp")
