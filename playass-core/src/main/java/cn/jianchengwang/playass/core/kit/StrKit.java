package cn.jianchengwang.playass.core.kit;

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
        return str == null || str.trim().length() == 0;
    }

}
