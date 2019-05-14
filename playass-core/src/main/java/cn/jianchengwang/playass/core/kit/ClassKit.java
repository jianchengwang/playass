package cn.jianchengwang.playass.core.kit;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

@Slf4j
public class ClassKit {

    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";

    private final static Map<String, Class> CACHE_CLASS_MAP = new LinkedHashMap<>();

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className) {
        try {
            if(!CACHE_CLASS_MAP.containsKey(className)) {
                CACHE_CLASS_MAP.put(className,  Class.forName(className));
            }
            return CACHE_CLASS_MAP.get(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            Class<?> clazz = loadClass(className);
            return (T) clazz.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }


    public static void setField(Field field, Object target, Object value) {
        setField(field, target, value, true);
    }

    public static void setField(Field field, Object target, Object value, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("setField error", e);
            throw new RuntimeException(e);
        }
    }

    public static Set<Class<?>> getPackageClass(String basePackage) {
        URL url = getClassLoader()
                .getResource(basePackage.replace(".", "/"));
        if (null == url) {
            throw new RuntimeException("无法获取项目路径文件");
        }
        try {
            if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
                // 若为普通文件夹，则遍历
                File file = new File(url.getFile());
                Path basePath = file.toPath();
                return Files.walk(basePath)
                        .filter(path -> path.toFile().getName().endsWith(".class"))
                        .map(path -> getClassByPath(path, basePath, basePackage))
                        .collect(Collectors.toSet());
            } else if (url.getProtocol().equalsIgnoreCase(JAR_PROTOCOL)) {
                // 若在 jar 包中，则解析 jar 包中的 entry
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                return jarURLConnection.getJarFile()
                        .stream()
                        .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                        .map(ClassKit::getClassByJar)
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (IOException e) {
            log.error("load package error", e);
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getClassByPath(Path classPath, Path basePath, String basePackage) {
        String packageName = classPath.toString().replace(basePath.toString(), "");
        String className = (basePackage + packageName)
                .replace("/", ".")
                .replace("\\", ".")
                .replace(".class", "");
        return loadClass(className);
    }

    private static Class<?> getClassByJar(JarEntry jarEntry) {
        String jarEntryName = jarEntry.getName();
        // 获取类名
        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
        return loadClass(className);
    }

    public static void invokeSetMethod(Class clazz, Field field, Object instance, Object value) throws Exception {

        String methodName = "set" + StrKit.upperCase(field.getName());

        Method method = clazz.getMethod(methodName, field.getType());

        switch (field.getType().getName()) {
            case "java.lang.String":
                method.invoke(instance, (String) value);
                break;
            case "java.lang.Integer":
                method.invoke(instance, Integer.valueOf(value.toString()));
                break;
            case "java.lang.Long":
                method.invoke(instance, Long.valueOf(value.toString()));
                break;
            case "java.lang.Double":
                method.invoke(instance, Double.valueOf(value.toString()));
                break;
            case "java.lang.Float":
                method.invoke(instance, Float.valueOf(value.toString()));
                break;
            case "java.lang.Boolean":
                method.invoke(instance, Boolean.valueOf(value.toString()));
                break;
            default:
                method.invoke(instance, value);
                break;

        }
    }

    public static Object invokeGetMethod(Class clazz, Field field, Object instance) throws Exception {

        String methodName = "set" + StrKit.upperCase(field.getName());

        Method method = clazz.getMethod(methodName, null);

        return method.invoke(instance, null);
    }

}
