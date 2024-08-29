plugins {
  id("otel.javaagent-instrumentation")
}

muzzle {
  pass {
    group.set("org.apache.camel")
    module.set("camel-api")
    versions.set("[4.4,]")
    assertInverse.set(true)
  }
}

//sourceSets {
//  main {
//    val shadedDep = project(":instrumentation:camel-4:library-instrumentation-shaded")
//    output.dir(
//      shadedDep.file("build/extracted/shadow"),
//      "builtBy" to ":instrumentation:camel-4:library-instrumentation-shaded:extractShadowJar"
//    )
//  }
//}

val camelversion = "4.4.3"

description = "camel-4"

dependencies {
  library("org.apache.camel:camel-api:$camelversion")
  implementation("org.apache.camel:camel-management-api:$camelversion")
  implementation("org.apache.camel:camel-opentelemetry:$camelversion")
  implementation("org.apache.camel:camel-tracing:$camelversion")
  //compileOnly(project(":instrumentation:camel-4:library-instrumentation-shaded", configuration = "shadow"))
  compileOnly("com.google.auto.value:auto-value-annotations")
  annotationProcessor("com.google.auto.value:auto-value")
}

