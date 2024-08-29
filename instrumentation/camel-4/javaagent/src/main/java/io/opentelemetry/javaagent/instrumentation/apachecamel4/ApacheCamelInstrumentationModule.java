/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.instrumentation.apachecamel4;

import static java.util.Collections.singletonList;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import java.util.Collections;
import java.util.List;

@AutoService(InstrumentationModule.class)
public class ApacheCamelInstrumentationModule extends InstrumentationModule {

  public ApacheCamelInstrumentationModule() {
    super("camel", "camel-4");
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return singletonList(new CamelContextInstrumentation());
  }

  @Override
  public boolean isHelperClass(String className) {
    return className.startsWith("org.apache.camel.");
  }

  @Override
  public List<String> getAdditionalHelperClassNames() {
    return Collections.singletonList(SemanticAttributes.class.getName());
  }

}
