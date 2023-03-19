package nl.snoworange.cranberry.util;

import java.awt.*;

public class ColorUtils {

    public static int rainbow(int delay, int saturation, int brightness) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 10.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), (float) saturation / 360.0f, (float) brightness / 360.0f).getRGB();
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
