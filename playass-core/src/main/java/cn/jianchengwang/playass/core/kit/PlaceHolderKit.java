package cn.jianchengwang.playass.core.kit;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PlaceHolderKit {

    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");
    private static Matcher matcher;

    /**
     * 替换字符串占位符, 字符串中使用{key}表示占位符
     *
     * @param sourceString 需要匹配的字符串，示例："名字:{name},年龄:{age},学校:{school}";
     * @param param        参数集,Map类型
     * @return
     */
    public static String replaceWithMap(String sourceString, Map<String, Object> param) {
        if (StrKit.isEmpty(sourceString) ||
            param.isEmpty()) {
            return sourceString;
        }

        String targetString = sourceString;
        matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            try {
                String key = matcher.group();
                String keyclone = key.substring(1, key.length() - 1).trim();
                Object value = param.get(keyclone);
                if (value != null) {
                    targetString = targetString.replace(key, value.toString());
                }
            } catch (Exception e) {
                throw new RuntimeException("String formatter failed", e);
            }
        }
        return targetString;
    }

    /**
     * 替换字符串占位符, 字符串中使用{key}表示占位符
     * <p>
     * 利用反射 自动获取对象属性值 (必须有get方法)
     *
     * @param sourceString 需要匹配的字符串
     * @param param        参数集
     * @return
     */
    public static String replaceWithObject(String sourceString, Object param) {
        if (StrKit.isEmpty(sourceString) || param == null) {
            return sourceString;
        }

        String targetString = sourceString;

        PropertyDescriptor pd;
        Method getMethod;

        // 匹配{}中间的内容 包括括号
        matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            String key = matcher.group();
            String holderName = key.substring(1, key.length() - 1).trim();
            try {
                pd = new PropertyDescriptor(holderName, param.getClass());
                getMethod = pd.getReadMethod(); // 获得get方法
                Object value = getMethod.invoke(param);
                if (value != null) {
                    targetString = targetString.replace(key, value.toString());
                }
            } catch (Exception e) {
                throw new RuntimeException("String formatter failed", e);
            }
        }
        return targetString;
    }

    /**
     * 查找String中的占位符keys；<br/>
     * 示例： "名字:{name},年龄:{age},学校:{school}"， 则返回：Set[name,age,school]
     * <p>
     * pattern示例：
     * <pre> {@code
     *  // 尖括号：<placeHolder> 表示为占位符
     *  Pattern pattern = Pattern.compile("\\<(.*?)\\>");
     *
     *  // 大括号：{placeHolder} 表示为占位符， 上面的示例中就使用{}作为占位符
     *  Pattern pattern = Pattern.compile("\\{(.*?)\\}");
     * }
     * </pre>
     *
     * @param sourceString
     * @return
     */
    public static Set<String> findPlaceHolderKeys(String sourceString) {
        Set<String> placeHolderSet = new HashSet<>();

        if (StrKit.isEmpty(sourceString)) {
            return placeHolderSet;
        }

        String targetString = sourceString;
        matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            String key = matcher.group();  //示例: {name}
            String placeHolder = key.substring(1, key.length() - 1).trim();  //示例： name
            placeHolderSet.add(placeHolder);
        }

        return placeHolderSet;
    }

}
