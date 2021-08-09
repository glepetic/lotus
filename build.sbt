name := "lotus"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("org.maple")

resolvers += Resolver.JCenterRepository
resolvers += Resolver.sonatypeRepo("snapshots")
//resolvers += Resolver.sonatypeRepo("snapshot")
resolvers += "dv8tion" at "https://m2.dv8tion.net/releases"

libraryDependencies += "net.katsstuff" %% "ackcord" % "0.18.0-SNAPSHOT"
libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.1.1"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

excludeDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl"

enablePlugins(JavaAppPackaging)