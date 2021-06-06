package ng.jlp.pricereduction.data;

import org.junit.Test;

import static ng.jlp.pricereduction.TestTools.assertColorSwatch;

/**
 * Tests for the ColorSwatch data holder.
 */
public class ColorSwatchTest {

    /**
     * These tests construct the ColorSwatch from the information given, and do the conversion from the basicColor
     * to the rgbColor. The basicColor value can be in any case (upper, lower, camel) and will map to the rgbColor.
     */
    @Test
    public void testColorSwatch_withColor_basicColorBlue_lowerCase() {
        ColorSwatch swatch = new ColorSwatch("blue", "blue", "1234");
        assertColorSwatch(swatch, "blue", "0000FF", "1234");
    }

    @Test
    public void testColorSwatch_noColor_basicColorGrey_camelCase() {
        ColorSwatch swatch = new ColorSwatch("", "Grey", "1234");
        assertColorSwatch(swatch, "", "808080", "1234");
    }

    @Test
    public void testColorSwatch_withColor_basicColorPurple_upperCase() {
        ColorSwatch swatch = new ColorSwatch("Purple", "PURPLE", "1234");
        assertColorSwatch(swatch, "Purple", "800080", "1234");
    }

    @Test
    public void testColorSwatch_withColor_basicColorMulti() {
        ColorSwatch swatch = new ColorSwatch("Multi", "Multi", "1234");
        assertColorSwatch(swatch, "Multi", "", "1234");
    }

    @Test
    public void testColorSwatch_withColor_basicColorEmpty() {
        ColorSwatch swatch = new ColorSwatch("Purple", "", "1234");
        assertColorSwatch(swatch, "Purple", "", "1234");
    }
}
