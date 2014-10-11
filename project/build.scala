import sbt._
import Keys._
import sbtrelease.ReleasePlugin._
import xerial.sbt.Sonatype._
import SonatypeKeys._

object build extends Build {
  type Sett = Def.Setting[_]

  val base = Defaults.defaultSettings ++ ScalaSettings.all ++ Seq[Sett](
      name := "rng"
    , organization := "com.github.japgolly.fork.nicta"
  )

  import scala.scalajs.sbtplugin.ScalaJSPlugin._

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
    , scalacOptions += sourceMapOpt
    , libraryDependencies ++= Seq(
      "com.github.japgolly.fork.scalaz"      %%% "scalaz-core"   % "7.1.0",
      "com.github.japgolly.fork.scalaz"      %%% "scalaz-effect" % "7.1.0")
    ) ++
    net.virtualvoid.sbt.graph.Plugin.graphSettings ++
    scalaJSBuildSettings ++
    sonatypeSettings
  )

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
