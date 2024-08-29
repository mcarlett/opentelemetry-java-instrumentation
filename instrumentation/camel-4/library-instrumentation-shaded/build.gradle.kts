plugins {
  id("com.github.johnrengelman.shadow")

  id("otel.java-conventions")
}

val camelversion = "4.4.3"

group = "io.opentelemetry.javaagent.instrumentation"

dependencies {
  compileOnly("org.apache.camel:camel-api:$camelversion")
  compileOnly("org.apache.camel:camel-management-api:$camelversion")
  implementation("org.apache.camel:camel-opentelemetry:$camelversion")
  implementation("org.apache.camel:camel-tracing:$camelversion")
}

tasks {
  shadowJar {
    exclude("META-INF/services/*")

    dependencies {
      include(dependency("org.apache.camel:camel-opentelemetry"))
      include(dependency("org.apache.camel:camel-tracing"))
    }
    relocate("org.apache.camel.opentelemetry", "io.opentelemetry.javaagent.instrumentation.apachecamel4.shaded.org.apache.camel.opentelemetry")
    relocate("org.apache.camel.tracing", "io.opentelemetry.javaagent.instrumentation.apachecamel4.shaded.org.apache.camel.tracing")
  }

  val extractShadowJar by registering(Copy::class) {
    dependsOn(shadowJar)
    from(zipTree(shadowJar.get().archiveFile))
    into("build/extracted/shadow")
  }
}
