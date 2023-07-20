package io.github.gaming32.niceload.api;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "niceload")
public class NiceLoadConfig implements ConfigData {
    public int[] backgroundColor = new int[]{ 239, 50, 61, 255 };
    public int[] barColor = new int[]{ 255, 255, 255 };
    public int[] textColor = new int[]{ 0, 170, 0 };

    public static NiceLoadConfig INSTANCE;
}
