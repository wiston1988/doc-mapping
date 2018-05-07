package org.doc.mapping.spring.annotation;

import org.doc.mapping.util.Constants;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DocMappingRegistrar.class)
public @interface DocMappingBuilder {
  String id();
  String defaultDateFormat() default Constants.DEFAULT_DATE_FORMAT;
  String defaultNumberFormat() default Constants.DEFAULT_NUMBER_FORMAT;
  boolean ignoreError() default Constants.IGNORE_ERROR;
  boolean ignoreEmpty() default Constants.IGNORE_EMPTY;
  boolean headerSupport() default Constants.HEADER_SUPPORT;
  String buildStrategyClass() default "";
  String[] fieldRenders() default {};
}
