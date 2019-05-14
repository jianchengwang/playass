package cn.jianchengwang.playass.core.aop;

import cn.jianchengwang.playass.core.aop.advice.Advice;
import cn.jianchengwang.playass.core.aop.advice.AfterReturningAdvice;
import cn.jianchengwang.playass.core.aop.advice.MethodBeforeAdvice;
import cn.jianchengwang.playass.core.aop.advice.ThrowsAdvice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProxyAdvisor {

    private Advice advice;

    /**
     * 执行代理方法
     */
    public Object doProxy(Object target, Class<?> targetClass, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;

        if (advice instanceof MethodBeforeAdvice) {
            ((MethodBeforeAdvice) advice).before(targetClass, method, args);
        }
        try {
            //执行目标类的方法
            result = proxy.invokeSuper(target, args);
            if (advice instanceof AfterReturningAdvice) {
                ((AfterReturningAdvice) advice).afterReturning(targetClass, result, method, args);
            }
        } catch (Exception e) {
            if (advice instanceof ThrowsAdvice) {
                ((ThrowsAdvice) advice).afterThrowing(targetClass, method, args, e);
            } else {
                throw new Throwable(e);
            }
        }
        return result;
    }

}
