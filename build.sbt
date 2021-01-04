name := "LearnScala"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v", "-s")