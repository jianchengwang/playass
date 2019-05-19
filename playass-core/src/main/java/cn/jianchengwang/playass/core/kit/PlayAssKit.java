package cn.jianchengwang.playass.core.kit;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class PlayAssKit {

    private static boolean isWindows;
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    static {
        isWindows = System.getProperties().getProperty("os.name").toLowerCase().contains("win");
    }

    public static String getCurrentClassPath() {
        URL url = PlayAssKit.class.getResource("/");
        String path;
        if (null == url) {
            File f = new File(PlayAssKit.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            path = f.getPath();
        } else {
            path = url.getPath();
        }
        if (isWindows()) {
            return decode(path.replaceFirst("^/(.:/)", "$1"));
        }
        return decode(path);
    }

    private static String decode(String path) {
        try {
            return java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return path;
        }
    }

    public static boolean isWindows() {
        return isWindows;
    }

}
