package ng.jlp.pricereduction.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;

/**
 * Data holder for ColorSwatch
 */
@JsonPropertyOrder({ "color", "rgbColor", "skuid" })
public class ColorSwatch {
    private String color;
    private String rgbColor;
    private String skuid;

    static HashMap<String, String> basicToRGB;

    static {
        basicToRGB = new HashMap<>();
        basicToRGB.put("BLUE", "0000FF");
        basicToRGB.put("GREEN", "00FF00");
        basicToRGB.put("RED", "FF0000");
        basicToRGB.put("BLACK", "000000");
        basicToRGB.put("PURPLE", "800080");
        basicToRGB.put("PINK", "FFC0CB");
        basicToRGB.put("YELLOW", "FFFF00");
        basicToRGB.put("MULTI", "");        // It is not possible to have a multi RGB color
        basicToRGB.put("GREY", "808080");
        basicToRGB.put("WHITE", "FFFFFF");
    }

    public ColorSwatch(String color, String basicColor, String skuid) {
        this.color = color;
        this.rgbColor = getColorToRGB(basicColor);
        this.skuid = skuid;
    }

    public static String getColorToRGB(String basicColor) {
        // Default to empty string if the color isn't found
        return basicToRGB.getOrDefault(basicColor.toUpperCase(), "");
    }

    public String getColor() {
        return color;
    }

    public String getRgbColor() {
        return rgbColor;
    }

    public String getSkuid() {
        return skuid;
    }
}
