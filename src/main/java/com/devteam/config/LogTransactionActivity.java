package com.devteam.config;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTransactionActivity {
  @AliasFor("name")
  String value() default "";

  @AliasFor("value")
  String name() default "";

  String label() default "";
}
