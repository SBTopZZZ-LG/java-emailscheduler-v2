package Utilities;

import java.awt.*;
import java.io.File;

public class Fonts {
    public static Font getBold() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/RobotoCondensed/RobotoCondensed-Bold.ttf"));
        } catch (Exception e) {
            return null;
        }
    }

    public static Font getRegular() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/RobotoCondensed/RobotoCondensed-Regular.ttf"));
        } catch (Exception e) {
            return null;
        }
    }

    public static Font getLight() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/RobotoCondensed/RobotoCondensed-Light.ttf"));
        } catch (Exception e) {
            return null;
        }
    }
}
