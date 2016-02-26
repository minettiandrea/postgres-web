enablePlugins(ScalaJSPlugin)

enablePlugins(SbtWeb)

name := "postgres-web"

version := "1.0"

scalaVersion := "2.11.7"


// create launcher file ( its search for object extends JSApp , make sure there is only one file)
persistLauncher := true

persistLauncher in Test := false

val scalaJSReactVersion = "0.10.4"

val scalaCssVersion = "0.3.1"

val reactJSVersion = "0.14.7"

val circeVersion = "0.3.0"

resolvers += "Madoushi sbt-plugins" at "https://dl.bintray.com/madoushi/sbt-plugins/"

libraryDependencies ++= Seq("com.github.japgolly.scalajs-react" %%% "core" % scalaJSReactVersion,
  "com.github.japgolly.scalajs-react" %%% "extra" % scalaJSReactVersion,
  "com.github.japgolly.scalacss" %%% "core" % scalaCssVersion,
  "com.github.japgolly.scalacss" %%% "ext-react" % scalaCssVersion,
  //"com.lihaoyi" %%% "upickle" % "0.3.8",
  "io.circe" %%% "circe-core" % circeVersion,
  "io.circe" %%% "circe-generic" % circeVersion,
  "io.circe" %%% "circe-parser" % circeVersion,
  "io.circe" %%% "circe-scalajs" % circeVersion,
  "org.scalatest" %%% "scalatest" % "3.0.0-M10" % "test"
)

// React itself
//   (react-with-addons.js can be react.js, react.min.js, react-with-addons.min.js)
//DOM, which doesn't exist by default in the Rhino runner. To make the DOM available in Rhino
jsDependencies ++= Seq(
  "org.webjars.npm" % "react"     % reactJSVersion / "react-with-addons.js" commonJSName "React"    minified "react-with-addons.min.js",
  "org.webjars.npm" % "react-dom" % reactJSVersion / "react-dom.js"         commonJSName "ReactDOM" minified "react-dom.min.js" dependsOn "react-with-addons.js",
  "org.webjars.npm" % "material-design-lite" % "1.1.1" / "dist/material.min.js"//,
//  "org.webjars.npm" % "jsonschema" % "1.1.0" / "validator.js",
//  "org.webjars.npm" % "react-jsonschema-form" % "0.11.0" / "react-jsonschema-form.js" dependsOn "validator.js"
)


// creates single js resource file for easy integration in html page
skip in packageJSDependencies := false


// copy  javascript files to js folder,that are generated using fastOptJS/fullOptJS

crossTarget in (Compile, fullOptJS) := file("js")

crossTarget in (Compile, fastOptJS) := file("js")

crossTarget in (Compile, packageJSDependencies) := file("js")

crossTarget in (Compile, packageScalaJSLauncher) := file("js")

crossTarget in (Compile, packageMinifiedJSDependencies) := file("js")

artifactPath in (Compile, fastOptJS) := ((crossTarget in (Compile, fastOptJS)).value /
  ((moduleName in fastOptJS).value + "-opt.js"))


scalacOptions += "-feature"

// Indicate that unit tests will access the DOM
requiresDOM := true

// Compile tests to JS using fast-optimisation
scalaJSStage in Test := FastOptStage

fullClasspath in Test ~= { _.filter(_.data.exists) }