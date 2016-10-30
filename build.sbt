name := "monitoring-akka-streams-kamon"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo

resolvers += "Kamon Repository Snapshots" at "http://snapshots.kamon.io"

libraryDependencies ++= {
  val akkaVersion = "2.4.12"
  val akkaHttpVersion = "2.4.11"
  val kamonVersion = "0.6.3"
  val kamonPrometheusVersion = "0.2.0"
  val typesafeConfigVersion = "1.3.0"
  val sprayVersion = "1.3.3"
  val sprayJsonVersion = "1.3.2"
  val logbackVersion = "1.1.5"
  val scalaTestVersion = "3.0.0"
  val junitVersion = "4.12"
  Seq(
    "com.typesafe" % "config" % typesafeConfigVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    // HTTP
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaHttpVersion,
    // CLUSTER
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    // SPRAY
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-http" % sprayVersion,
    "io.spray" %% "spray-json" % sprayJsonVersion,
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

addCommandAlias("runSingle", "runMain com.michalplachta.shoesorter.api.SingleNodeApp")

addCommandAlias("runSharded", "runMain com.michalplachta.shoesorter.api.ShardedApp")

// AspectJ Weaver is required by Kamon Scala/Akka
aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true
