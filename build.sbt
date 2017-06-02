name := "monitoring-akka-prometheus-kamon"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.9"

mainClass in Compile := Some("com.michalplachta.shoesorter.api.SingleNodeApp")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaVersion = "2.4.18"
  val akkaHttpVersion = "10.0.6"
  val aspectjweaverV = "1.8.10"
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
    "org.aspectj" % "aspectjweaver" % aspectjweaverV,
    "io.kamon" %% "kamon-core" % kamonVersion,
    "io.kamon" %% "kamon-scala" % kamonVersion,
    "io.kamon" %% "kamon-akka" % kamonVersion,
    "io.kamon" %% "kamon-akka-remote_akka-2.4" % kamonVersion,
    "io.kamon" %% "kamon-autoweave" % kamonVersion,
    "com.monsanto.arch" %% "kamon-prometheus" % kamonPrometheusVersion,
    // TESTING
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "junit" % "junit" % junitVersion % Test
  )
}

retrieveManaged := true

// Create a new MergeStrategy for aop.xml files
val aopMerge = new sbtassembly.MergeStrategy {
  val name = "aopMerge"
  import scala.xml._
  import scala.xml.dtd._

  def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
    val dt = DocType("aspectj", PublicID("-//AspectJ//DTD//EN", "http://www.eclipse.org/aspectj/dtd/aspectj.dtd"), Nil)
    val file = MergeStrategy.createMergeTarget(tempDir, path)
    val xmls: Seq[Elem] = files.map(XML.loadFile)
    val aspectsChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "aspects" \ "_")
    val weaverChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "weaver" \ "_")
    val options: String = xmls.map(x => (x \\ "aspectj" \ "weaver" \ "@options").text).mkString(" ").trim
    val weaverAttr = if (options.isEmpty) Null else new UnprefixedAttribute("options", options, Null)
    val aspects = new Elem(null, "aspects", Null, TopScope, false, aspectsChildren: _*)
    val weaver = new Elem(null, "weaver", weaverAttr, TopScope, false, weaverChildren: _*)
    val aspectj = new Elem(null, "aspectj", Null, TopScope, false, aspects, weaver)
    XML.save(file.toString, aspectj, "UTF-8", xmlDecl = false, dt)
    IO.append(file, IO.Newline.getBytes(IO.defaultCharset))
    Right(Seq(file -> path))
  }
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", "aop.xml") => aopMerge
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}

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
