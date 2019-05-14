package cn.jianchengwang.playass.core.ioc.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String value() default "";

    @Deprecated
    boolean singleton() default true;

}
