package io.github.gaming32.niceload.api;

public class NiceLoadTomlConfig {
    public String backgroundColor;
    public String barColor;
    public String textColor;

    private static String getRgbHexString(int value) {
        return String.format("%06X", 0xFFFFFF & value);
    }

    public static NiceLoadTomlConfig from(NiceLoadConfig configIn) {
        NiceLoadTomlConfig config = new NiceLoadTomlConfig();

        config.backgroundColor = "0x" + getRgbHexString(configIn.backgroundColor);
        config.barColor = "0x" + getRgbHexString(configIn.barColor);
        config.textColor = "0x" + getRgbHexString(configIn.textColor);

        return config;
    }

    public NiceLoadConfig toConfig() {
        NiceLoadConfig config = new NiceLoadConfig();

        if (this.backgroundColor == null)
            this.backgroundColor = Integer.toHexString(config.backgroundColor);

        if (this.barColor == null)
            this.barColor = Integer.toHexString(config.barColor);

        if (this.textColor == null)
            this.textColor = Integer.toHexString(config.textColor);

        config.backgroundColor = Integer.valueOf(this.backgroundColor.replace("0x", ""), 16);
        config.barColor = Integer.valueOf(this.barColor.replace("0x", ""), 16);
        config.textColor = Integer.valueOf(this.textColor.replace("0x", ""), 16);

        return config;
    }
}
