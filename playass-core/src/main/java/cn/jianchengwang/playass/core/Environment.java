package cn.jianchengwang.playass.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Environment {


    private static final String      PREFIX_CLASSPATH = "classpath:";
    private static final String      PREFIX_FILE      = "file:";
    private static final String      PREFIX_URL       = "url:";
    private static final Environment EMPTY_ENV        = new Environment();




}
