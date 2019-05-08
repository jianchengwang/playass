package cn.jianchengwang.playass.core;


import cn.jianchengwang.playass.core.mvc.route.RouteInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Const {


    /**
     * 默认字符集
     */
    public static final String DEFAULT_CHAR_SET = "UTF-8";

    /**
     * 当前版本号
     */
    public static final String MARIO_VERSION = "1.0.0";

    public static final ObjectMapper MAPPER = new ObjectMapper();

}
