logLevel := Level.Warn

resolvers += Resolver.sonatypeRepo("public")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.2.1")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.0-M5")
