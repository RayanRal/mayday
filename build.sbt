
name := "Mayday"

version := "1.0"

scalaVersion := "2.11.7"


resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"


val spray_version = "1.3.3"


libraryDependencies ++= Seq(
  "io.spray" %% "spray-can" % spray_version,
  "io.spray" %% "spray-http" % spray_version,
  "io.spray" %% "spray-httpx" % spray_version,
  "io.spray" %% "spray-routing" % spray_version,
  "io.spray" %% "spray-client" % spray_version,
  "io.spray" %% "spray-testkit" % spray_version,

  "io.spray" %% "spray-json" % "1.2.6",

  "org.reactivemongo" %% "reactivemongo" % "0.11.7"

)