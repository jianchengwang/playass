package cn.jianchengwang.playass.core.mvc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Param {

    String value() default "";

    String defaultValue() default "";

    boolean notNull() default false;

    boolean notBlank() default false;

}
