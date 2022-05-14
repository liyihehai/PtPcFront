package com.nnte.pf_source.uti.request;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited

public @interface UtiURL {
    String url();
}
