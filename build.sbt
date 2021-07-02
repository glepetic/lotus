name := "lotus"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("org.maple")

resolvers += Resolver.JCenterRepository
libraryDependencies += "net.katsstuff" %% "ackcord" % "0.17.1"
libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"

enablePlugins(JavaAppPackaging)