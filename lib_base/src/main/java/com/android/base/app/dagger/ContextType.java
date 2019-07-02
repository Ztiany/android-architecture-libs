package com.android.base.app.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ContextType {

    String ACTIVITY = "Activity";
    String CONTEXT = "Context";
    String APPLICATION = "Application";

    String value() default APPLICATION;

}
