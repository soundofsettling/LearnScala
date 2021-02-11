name := "LearnScala"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "com.lihaoyi" %% "requests" % "0.6.5"
libraryDependencies += "com.lihaoyi" %% "ujson" % "0.9.5"
libraryDependencies += "org.asynchttpclient" % "async-http-client" % "2.12.2"
libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"

testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v", "-s")