name := "ScalaHeadlessCMS"

version := "0.1"

scalaVersion := "2.13.4"

val CatsVersion = "2.1.1"
val CatsEffectVersion = "2.1.1"
val CirceVersion = "0.13.0"
val DoobieVersion = "0.9.0"
val FlywayVersion = "6.3.1"
val H2Version = "1.4.200"
val Http4sVersion = "0.21.11"
val LogbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % CatsVersion,
  "org.typelevel" %% "cats-effect" % CatsEffectVersion,

  "io.circe" %% "circe-generic" % CirceVersion,

  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,

  "com.h2database" % "h2" % H2Version,
  "org.flywaydb" % "flyway-core" % FlywayVersion,

  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,

  "ch.qos.logback" % "logback-classic" % LogbackVersion,
)

scalacOptions ++= Seq(
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-deprecation",
  "-Xfatal-warnings",
  "-Ywarn-value-discard",
  "-Xlint:missing-interpolator",
)
