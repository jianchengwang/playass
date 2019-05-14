package cn.jianchengwang.playass.core.kit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrKit {

    public static String upperCase(String name, int... index) {
        char[] cs = name.toCharArray();
        if(index.length == 0) index = new int[]{0};
        for(int i=0; i<index.length; i++) {
            cs[index[i]]-=32;
        }

        return String.valueOf(cs);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String removeHeadTailChar(String str) {
        return str.substring(1, str.length() - 1);
    }

}
