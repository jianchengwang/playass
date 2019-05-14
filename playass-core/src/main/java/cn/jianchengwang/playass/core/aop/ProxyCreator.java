package cn.jianchengwang.playass.core.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyCreator {

    public static Object createProxy(Class<?> targetClass, ProxyAdvisor proxyAdvisor) {
        return Enhancer.create(targetClass,
                (MethodInterceptor) (target, method, args, proxy) ->
                        proxyAdvisor.doProxy(target, targetClass, method, args, proxy));
    }
}
