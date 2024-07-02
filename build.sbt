import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.*
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging


name := "scala-portfolio"
version := "1.0.0"
scalaVersion := "2.13.14"
idePackagePrefix := Some("eoi.de.examples")

val VersionSpark = "3.5.1"
val VersionCatsCore = "2.12.0"
val VersionScalaTest = "3.2.18"
val VersionScalafmt = "3.8.1"


val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % VersionSpark % Provided,
  "org.apache.spark" %% "spark-sql" % VersionSpark % Provided,
  "org.apache.spark" %% "spark-graphx" % VersionSpark % Provided,
  "org.apache.spark" %% "spark-avro" % VersionSpark % Provided,
  // Delta lake
  "io.delta" %% "delta-spark" % "3.2.0" % Provided,
)

val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % VersionCatsCore
  // Cats relies on improved type inference via the fix for SI-2712, which is not enabled by default.
  // For Scala 2.12 you should add the following to your build.sbt:
  // scalacOptions += "-Ypartial-unification"
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % VersionScalaTest % "test",
  "com.github.mrpowers" %% "spark-fast-tests" % "1.3.0" % "test"
)

val jmhDependencies = Seq(
  "org.openjdk.jmh" % "jmh-core" % "1.37",
  "org.openjdk.jmh" % "jmh-generator-annprocess" % "1.37",
)

val scalaFmtDependencies = Seq(
  "org.scalameta" %% "scalafmt-sysops" % VersionScalafmt,
  "org.scalameta" %% "scalafmt-dynamic" % VersionScalafmt,

)

// Spark Testing
Test / fork := true
Test / parallelExecution := false

libraryDependencies ++= sparkDependencies ++ catsDependencies ++ testDependencies ++
  jmhDependencies ++ scalaFmtDependencies

// Add JVM Options
javaOptions ++= Seq(
  "-Xmx2G",
  "-Xms2G",
  "-XX:ReservedCodeCacheSize=512M",
  "-XX:+UseG1GC",
  "-XX:MaxGCPauseMillis=200",
  "-XX:G1ReservePercent=15",
  "-XX:InitiatingHeapOccupancyPercent=25",
  "-XX:+UseStringDeduplication"
)
// build.sbt
enablePlugins(JmhPlugin)
// Add JMH alias: -i 3 means 3 iterations, -wi 2 means 2 warm-up iterations, -f1 means 1 fork, -t1 means 1 thread
addCommandAlias("jmh", "Jmh/run -i 3 -wi 2 -f1 -t1")
// Add JMH alias with profiler options: -prof gc means garbage collection profiler
// -prof cl means class loading profiler
// -prof comp means compiler profiler
// -prof hs_gc means HotSpot garbage collection profiler
// -prof hs_cl means HotSpot class loading profiler
// -prof hs_comp means HotSpot compiler profiler
addCommandAlias("prof", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof gc")
addCommandAlias("prof-cl", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof cl")
addCommandAlias("prof-comp", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof comp")
addCommandAlias("prof-gc", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof gc")
// All the profilers
addCommandAlias("prof-all", "Jmh/run -i 3 -wi 2 -f1 -t1 -prof gc -prof cl -prof comp")


// Empaquetar la aplicación
enablePlugins(JavaAppPackaging)

dockerBaseImage := "openjdk:11-jre-slim"
Docker / version := "latest"
dockerExposedPorts := Seq(4040)
dockerUpdateLatest := true
dockerRepository := Some("eoi.de")
dockerEntrypoint := Seq("java", "-jar", "/opt/docker/lib/scala-portfolio.jar", "eoi.de.examples.spark.sql.datasets.EjemploDatasetsApp03") // Llamamos a la clase principal de la aplicación: eoi.de.examples.spark.sql.datasets.EjemploDatasetsApp03

dockerUpdateLatest := true
Docker / maintainer := "eoi.de"

// Copiamos el jar generado por sbt-assembly al directorio de Docker
Docker / stage := {
  // targetDir es el directorio de salida de la aplicación
  val targetDir = stage.value
  // assemblyOutputPath es el nombre del jar generado por sbt-assembly
  val jarFile = (assembly / assemblyOutputPath).value
  // Copiamos el jar al directorio de Docker
  IO.copyFile(jarFile, targetDir / "lib" / jarFile.getName)
  targetDir
}

// Añadimos el plugin de assembly
enablePlugins(AssemblyPlugin)
assembly / assemblyJarName := "scala-portfolio.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => (assembly / assemblyMergeStrategy).value(x)
}



// Añadimos command alias para el plugin de Docker
// Primero hay que empaquetar la aplicación: assembly
addCommandAlias("publicar-docker", ";assembly;docker:publishLocal")


