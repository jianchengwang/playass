package cn.jianchengwang.playass.core.mvc.annotation.action;

import cn.jianchengwang.playass.core.mvc.http.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetAction {

    String value() default "";

    HttpMethod[] method() default {HttpMethod.GET};

}

