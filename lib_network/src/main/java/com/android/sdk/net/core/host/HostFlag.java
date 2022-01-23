package com.android.sdk.net.core.host;

import com.android.sdk.net.NetContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HostFlag {

    String value() default (NetContext.DEFAULT_FLAG);

}
