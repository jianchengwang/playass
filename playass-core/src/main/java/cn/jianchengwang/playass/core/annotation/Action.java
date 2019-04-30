package cn.jianchengwang.playass.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Action {
    String value() default "";
    String method() default "GET";
}
