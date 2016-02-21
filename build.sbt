name := "monitoring-akka-streams-kamon"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaVersion = "2.4.2"
  val kamonVersion = "0.5.2"
  val typesafeConfigVersion = "1.3.0"
  val logbackVersion = "1.1.5"
  val scalaTestVersion = "2.2.5"
  val junitVersion = "4.10"
  Seq(
    "com.typesafe" % "config" % typesafeConfigVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    // STREAM
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
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
    "io.kamon" %% "kamon-log-reporter" % kamonVersion,
    // TESTING
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "junit" % "junit" % junitVersion % Test
  )
}

addCommandAlias("runSingle", "runMain com.michalplachta.shoesorter.api.SingleNodeApp")

addCommandAlias("runSharded", "runMain com.michalplachta.shoesorter.api.ShardedApp")

fork in run := true
