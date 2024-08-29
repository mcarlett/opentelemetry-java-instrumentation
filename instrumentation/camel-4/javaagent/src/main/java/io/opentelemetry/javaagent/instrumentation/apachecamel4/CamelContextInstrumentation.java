/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.instrumentation.apachecamel4;

import static io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers.hasClassesNamed;
import static io.opentelemetry.javaagent.extension.matcher.AgentElementMatchers.implementsInterface;
import static net.bytebuddy.matcher.ElementMatchers.isPublic;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.camel.CamelContext;
import org.apache.camel.opentelemetry.OpenTelemetryTracer;
import org.apache.camel.support.CamelContextHelper;

public class CamelContextInstrumentation implements TypeInstrumentation {

  @Override
  public ElementMatcher<ClassLoader> classLoaderOptimization() {
    return hasClassesNamed("org.apache.camel.CamelContext");
  }

  @Override
  public ElementMatcher<TypeDescription> typeMatcher() {
    return implementsInterface(named("org.apache.camel.CamelContext"));
  }

  @Override
  public void transform(TypeTransformer transformer) {
    transformer.applyAdviceToMethod(
        named("start").and(isPublic()).and(takesArguments(0)),
        this.getClass().getName() + "$StartAdvice");
  }

  @SuppressWarnings("unused")
  public static class StartAdvice {

    @Advice.OnMethodEnter()
    public static void onContextStart(@Advice.This CamelContext context) {
      System.out.println("onContextStart before if");
      if (context.hasService(OpenTelemetryTracer.class) == null) {
        System.out.println("onContextStart");
        OpenTelemetryTracer openTelemetryTracer = new OpenTelemetryTracer();
        openTelemetryTracer.setInstrumentationName("camel");
        Tracer tracer = CamelContextHelper.findSingleByType(context, Tracer.class);
        if (tracer != null) {
          openTelemetryTracer.setTracer(tracer);
        }
        ContextPropagators contextPropagators = CamelContextHelper.findSingleByType(context,
            ContextPropagators.class);
        if (contextPropagators != null) {
          openTelemetryTracer.setContextPropagators(contextPropagators);
        }
        openTelemetryTracer.init(context);
        System.out.println("Camel OpenTelemetryTracer initialized");
      }
    }
  }
}
