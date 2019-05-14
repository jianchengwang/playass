package cn.jianchengwang.playass.core.aop.advice;

import java.lang.reflect.Method;

public interface ThrowsAdvice extends Advice{

    void afterThrowing(Class<?> clz, Method method, Object[] args, Throwable e);

}
