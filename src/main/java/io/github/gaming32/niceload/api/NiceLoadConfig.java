package io.github.gaming32.niceload.api;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "niceload")
public class NiceLoadConfig implements ConfigData {
    @ConfigEntry.ColorPicker
    public int backgroundColor = 0xef323d;

    @ConfigEntry.ColorPicker
    public int barColor = 0xffffff;

    @ConfigEntry.ColorPicker
    public int textColor = 0x00aa00;

    public static NiceLoadConfig INSTANCE;
}
