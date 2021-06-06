package ng.jlp.pricereduction.data;

import org.junit.Test;

import java.util.ArrayList;

import static ng.jlp.pricereduction.TestTools.assertColorSwatch;
import static ng.jlp.pricereduction.TestTools.assertPrice;
import static ng.jlp.pricereduction.TestTools.assertProduct;
import static ng.jlp.pricereduction.TestTools.createColorSwatches;
import static ng.jlp.pricereduction.TestTools.createPriceWithWasNow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the Product data holder.
 */
public class ProductTest {

    @Test
    public void testProduct() {
        Product product = new Product("1234", "Test Product",
                createColorSwatches(), createPriceWithWasNow());

        assertProduct(product, "1234", "Test Product");

        // Validate ColorSwatch entries
        ArrayList<ColorSwatch> colorSwatches = product.getColorSwatches();
        assertNotNull(colorSwatches);
        assertEquals(3, colorSwatches.size());

        ColorSwatch swatch1 = colorSwatches.get(0);
        assertColorSwatch(swatch1, "Blue", "0000FF", "123");
        ColorSwatch swatch2 = colorSwatches.get(1);
        assertColorSwatch(swatch2, "Red", "FF0000", "456");
        ColorSwatch swatch3 = colorSwatches.get(2);
        assertColorSwatch(swatch3, "Green", "00FF00", "789");

        // Validate InputPrice entry
        Price inputPrice = product.getInputPrice();
        assertNotNull(inputPrice);
        assertPrice(inputPrice, 20.00, 0, 0, 10.00, "GBP");
    }
}
