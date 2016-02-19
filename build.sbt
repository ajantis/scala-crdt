name := "scala-crdt"

version := "1.0"

scalaVersion := "2.11.7"

organization := "io.dmitryivanov"

organizationHomepage := Some(url("http://dmitryivanov.io/"))

licenses := Seq("The MIT License (MIT)" -> url("https://opensource.org/licenses/MIT"))

scalacOptions := Seq("-encoding", "utf8",
  "-target:jvm-1.7",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-unchecked",
  "-deprecation",
  "-Xlog-reflective-calls",
  "-Ywarn-adapted-args"
)

libraryDependencies <++= scalaVersion { v: String =>
  val specs2Version = "2.4.15"
  Seq(
    "org.specs2" %%  "specs2" % specs2Version % "test"
  )
}
