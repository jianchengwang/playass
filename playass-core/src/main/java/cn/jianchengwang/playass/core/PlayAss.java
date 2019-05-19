package cn.jianchengwang.playass.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static cn.jianchengwang.playass.core.mvc.Const.*;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayAss {

    private Set<String> packages = new LinkedHashSet<>(PLUGIN_PACKAGE_NAME);
    private Set<String> statics = new HashSet<>(DEFAULT_STATICS);



}
