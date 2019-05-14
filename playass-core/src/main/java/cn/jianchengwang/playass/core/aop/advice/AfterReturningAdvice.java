package cn.jianchengwang.playass.core.aop.advice;

import java.lang.reflect.Method;

public interface AfterReturningAdvice extends Advice {

    void afterReturning(Class<?> clz, Object returnValue, Method method, Object[] args) throws Throwable;

}
