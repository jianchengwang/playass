package cn.jianchengwang.playass.core.mvc.annotation.action;

import cn.jianchengwang.playass.core.mvc.context.H;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Action {
    String value() default "";

    H.Method[] method() default {H.Method.ALL};
}
