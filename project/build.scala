import sbt._
import Keys._
import sbtrelease.ReleasePlugin._
import xerial.sbt.Sonatype._
import SonatypeKeys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.toScalaJSGroupID

object build extends Build {
  type Sett = Def.Setting[_]

  val base = Defaults.defaultSettings ++ ScalaSettings.all ++ Seq[Sett](
      name := "rng"
    , organization := "com.github.japgolly.fork.nicta"
  )

  val sourceMapOpt = {
    val a = new java.io.File("").toURI.toString.replaceFirst("/$", "")
    val g = "https://raw.githubusercontent.com/japgolly/rng/v1.3.0-js-1"
    s"-P:scalajs:mapSourceURI:$a->$g/"
  }


  val rng = Project(
    id = "rng"
  , base = file(".")
  , settings = base ++ ReplSettings.all ++ releaseSettings ++ PublishSettings.all ++ InfoSettings.all ++ Seq[Sett](
      name := "rng"
    , scalaVersion := "2.11.5"
    , scalacOptions += sourceMapOpt
    , libraryDependencies ++= Seq(
      "com.github.japgolly.fork.scalaz"      %%% "scalaz-core"   % "7.1.1-2",
      "com.github.japgolly.fork.scalaz"      %%% "scalaz-effect" % "7.1.1-2")
    ) ++
    net.virtualvoid.sbt.graph.Plugin.graphSettings ++
    ScalaJSPlugin.projectSettings ++
    sonatypeSettings
  ).enablePlugins(ScalaJSPlugin)

  // val examples = Project(
    // id = "examples"
  // , base = file("examples")
  // , dependencies = Seq(rng)
  // , settings = base ++ Seq[Sett](
      // name := "rng-examples"
    // , fork in run := true
    // , libraryDependencies ++= Seq(scalaz, scalazEffect)
    // , javaOptions in run <++= (fullClasspath in Runtime) map { cp => Seq("-cp", sbt.Attributed.data(cp).mkString(":")) }
    // , resolvers ++= Seq(Resolver.typesafeRepo("releases"))
    // )
  // )
}
