package io.devnindo.service.deploy.base;


import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // static final method
public @interface PreBoot {

    /** The name. */
    String config() default "";
}