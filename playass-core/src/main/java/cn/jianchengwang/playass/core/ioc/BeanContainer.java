package cn.jianchengwang.playass.core.ioc;

import cn.jianchengwang.playass.core.aop.annotation.Aspect;
import cn.jianchengwang.playass.core.ioc.annotation.Bean;
import cn.jianchengwang.playass.core.ioc.annotation.Inject;
import cn.jianchengwang.playass.core.kit.ClassKit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    private boolean isLoadBean = false;
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(Bean.class, Inject.class, Aspect.class);

    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder {
        HOLDER;
        private BeanContainer instance;

        ContainerHolder() {
            instance = new BeanContainer();
        }
    }

    public Object getBean(Class<?> clz) {
        if (null == clz) {
            return null;
        }
        return beanMap.get(clz);
    }

    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    public Object addBean(Class<?> clz, Object bean) {
        return beanMap.put(clz, bean);
    }

    public void removeBean(Class<?> clz) {
        beanMap.remove(clz);
    }

    public int size() {
        return beanMap.size();
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.keySet()
                .stream()
                .filter(clz -> clz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getClassesBySuper(Class<?> superClass) {
        return beanMap.keySet()
                .stream()
                .filter(superClass::isAssignableFrom)
                .filter(clz -> !clz.equals(superClass))
                .collect(Collectors.toSet());
    }

    public boolean isLoadBean() {
        return isLoadBean;
    }

    public void loadBeans(String basePackage) {
        if (isLoadBean()) {
            log.warn("bean已经加载");
            return;
        }

        Set<Class<?>> classSet = ClassKit.getPackageClass(basePackage);
        classSet.stream()
                .filter(clz -> {
                    for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                        if (clz.isAnnotationPresent(annotation)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(clz -> beanMap.put(clz, ClassKit.newInstance(clz.getName())));
        isLoadBean = true;
    }

}
