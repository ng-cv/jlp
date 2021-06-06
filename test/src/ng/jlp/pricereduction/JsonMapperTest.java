package ng.jlp.pricereduction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import ng.jlp.pricereduction.data.ColorSwatch;
import ng.jlp.pricereduction.data.Price;
import ng.jlp.pricereduction.data.Product;
import org.junit.Test;

import java.util.ArrayList;

import static ng.jlp.pricereduction.TestTools.assertColorSwatch;
import static ng.jlp.pricereduction.TestTools.assertPrice;
import static ng.jlp.pricereduction.TestTools.assertProduct;
import static ng.jlp.pricereduction.TestTools.createJsonProduct_multipleColorSwatches_basicNowPrice;
import static ng.jlp.pricereduction.TestTools.createJsonProduct_nowFromTo;
import static ng.jlp.pricereduction.TestTools.createJsonProduct_oneColorSwatch_basicNowPrice;
import static ng.jlp.pricereduction.TestTools.createJsonProducts_multipleProducts;
import static ng.jlp.pricereduction.TestTools.createJsonProducts_oneProduct;
import static ng.jlp.pricereduction.TestTools.createReducedProducts;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests for the JsonMapper class.
 */
public class JsonMapperTest {

    @Test
    public void testMapColorSwatchesFromJson_oneSwatch() {
        try {
            JsonNode node = createJsonProduct_oneColorSwatch_basicNowPrice();
            ArrayList<ColorSwatch> colorSwatches = JsonMapper.mapColorSwatchesFromJson(node);

            assertNotNull(colorSwatches);
            assertEquals(1, colorSwatches.size());

            // Validate the ColorSwatch
            ColorSwatch swatch = colorSwatches.get(0);
            assertColorSwatch(swatch, "Red", "FF0000", "236642844");
        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    @Test
    public void testMapColorSwatchesFromJson_multipleSwatches() {
        try {
            JsonNode node = createJsonProduct_multipleColorSwatches_basicNowPrice();
            ArrayList<ColorSwatch> colorSwatches = JsonMapper.mapColorSwatchesFromJson(node);

            assertNotNull(colorSwatches);
            assertEquals(4, colorSwatches.size());

            // Validate each of the ColorSwatches
            ColorSwatch swatch1 = colorSwatches.get(0);
            assertColorSwatch(swatch1, "Blue", "0000FF", "232556407");
            ColorSwatch swatch2 = colorSwatches.get(1);
            assertColorSwatch(swatch2, "Red", "FF0000", "236642844");
            ColorSwatch swatch3 = colorSwatches.get(2);
            assertColorSwatch(swatch3, "Green", "00FF00", "236642847");
            ColorSwatch swatch4 = colorSwatches.get(3);
            assertColorSwatch(swatch4, "Navy", "0000FF", "232556408");
        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    @Test
    public void testMapPriceFromJson_nowBasic() {
        try {
            JsonNode node = createJsonProduct_multipleColorSwatches_basicNowPrice();
            Price price = JsonMapper.mapPriceFromJson(node);

            assertNotNull(price);
            // Validate the Price values
            assertPrice(price, 129.00, 101.99, 0, 89.00, "GBP");

        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    /**
     * Test that when the 'now' price has a 'from' and 'to', the price.now will be set to 0
     */
    @Test
    public void testMapPriceFromJson_nowFromTo() {
        try {
            JsonNode node = createJsonProduct_nowFromTo();
            Price price = JsonMapper.mapPriceFromJson(node);

            assertNotNull(price);
            // Validate the Price values
            assertPrice(price, 129.00, 101.99, 0, 0, "GBP");

        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    @Test
    public void testMapProductsFromJson_oneProduct() {
        try {
            JsonNode node = createJsonProducts_oneProduct();
            ArrayList<Product> products = JsonMapper.mapProductsFromJson(node);

            // Validate the Products
            assertNotNull(products);
            assertEquals(1, products.size());
            Product product = products.get(0);
            assertProduct(product, "4919177", "John Lewis & Partners Jersey Bandeau Dress, Blue");

            // Validate the InputPrice
            Price price = product.getInputPrice();
            assertNotNull(price);
            assertPrice(price, 0, 0, 0, 29.00, "GBP");

            // Validate the ColorSwatches
            ArrayList<ColorSwatch> colorSwatches = product.getColorSwatches();
            assertNotNull(colorSwatches);
            assertEquals(1, colorSwatches.size());
            ColorSwatch swatch = colorSwatches.get(0);
            assertColorSwatch(swatch, "", "0000FF", "238460015");

        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    @Test
    public void testMapProductsFromJson_multipleProducts() {
        try {
            JsonNode node = createJsonProducts_multipleProducts();
            ArrayList<Product> products = JsonMapper.mapProductsFromJson(node);

            // Validate the Products
            assertNotNull(products);
            assertEquals(3, products.size());

            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // These products could be tested in a parameterised method to eliminate the duplication here, but haven't
            // done that due to time constraints.
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Product 1
            Product product1 = products.get(0);
            assertProduct(product1, "4919177", "John Lewis & Partners Jersey Bandeau Dress, Blue");

            // Validate the InputPrice
            Price price1 = product1.getInputPrice();
            assertNotNull(price1);
            assertPrice(price1, 0, 0, 0, 29.00, "GBP");

            // Validate the ColorSwatches
            ArrayList<ColorSwatch> colorSwatches1 = product1.getColorSwatches();
            assertNotNull(colorSwatches1);
            assertEquals(1, colorSwatches1.size());
            ColorSwatch swatch1 = colorSwatches1.get(0);
            assertColorSwatch(swatch1, "", "0000FF", "238460015");

            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Product 2
            Product product2 = products.get(1);
            assertProduct(product2, "4323134", "Kin Denim Shirt Dress, Blue");

            // Validate the InputPrice
            Price price2 = product2.getInputPrice();
            assertNotNull(price2);
            assertPrice(price2, 0, 0, 0, 85.00, "GBP");

            // Validate the ColorSwatches
            ArrayList<ColorSwatch> colorSwatches2 = product2.getColorSwatches();
            assertNotNull(colorSwatches2);
            assertEquals(1, colorSwatches2.size());
            ColorSwatch swatch2 = colorSwatches2.get(0);
            assertColorSwatch(swatch2, "", "0000FF", "238104109");

            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Product 3
            Product product3 = products.get(2);
            assertProduct(product3, "4957430", "Whistles Alba Shift Dress, Green");

            // Validate the InputPrice
            Price price3 = product3.getInputPrice();
            assertNotNull(price3);
            assertPrice(price3, 99.00, 0, 0, 50.00, "GBP");

            // Validate the ColorSwatches
            ArrayList<ColorSwatch> colorSwatches3 = product3.getColorSwatches();
            assertNotNull(colorSwatches3);
            assertEquals(1, colorSwatches3.size());
            ColorSwatch swatch3 = colorSwatches3.get(0);
            assertColorSwatch(swatch3, "", "00FF00", "238653932");

        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    @Test
    public void testMapProductsToJson() {
        ArrayList<Product> products = createReducedProducts();
        String result = JsonMapper.mapProductsToJson(products);

        String expected = "[ {\n" +
                "  \"productId\" : \"1\",\n" +
                "  \"title\" : \"item 1\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"Blue\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"123\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Red\",\n" +
                "    \"rgbColor\" : \"FF0000\",\n" +
                "    \"skuid\" : \"456\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Green\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"789\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 10.0,\n" +
                "  \"priceLabel\" : \"Was £20, now £10\"\n" +
                "}, {\n" +
                "  \"productId\" : \"2\",\n" +
                "  \"title\" : \"item 2\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"Blue\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"123\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Red\",\n" +
                "    \"rgbColor\" : \"FF0000\",\n" +
                "    \"skuid\" : \"456\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Green\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"789\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 10.0,\n" +
                "  \"priceLabel\" : \"Was £23, now £10\"\n" +
                "}, {\n" +
                "  \"productId\" : \"3\",\n" +
                "  \"title\" : \"item 3\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"Blue\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"123\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Red\",\n" +
                "    \"rgbColor\" : \"FF0000\",\n" +
                "    \"skuid\" : \"456\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Green\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"789\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 10.0,\n" +
                "  \"priceLabel\" : \"Was £11, now £10\"\n" +
                "}, {\n" +
                "  \"productId\" : \"4\",\n" +
                "  \"title\" : \"item 4\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"Blue\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"123\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Red\",\n" +
                "    \"rgbColor\" : \"FF0000\",\n" +
                "    \"skuid\" : \"456\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Green\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"789\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 10.0,\n" +
                "  \"priceLabel\" : \"Was £25, now £10\"\n" +
                "}, {\n" +
                "  \"productId\" : \"5\",\n" +
                "  \"title\" : \"item 5\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"Blue\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"123\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Red\",\n" +
                "    \"rgbColor\" : \"FF0000\",\n" +
                "    \"skuid\" : \"456\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Green\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"789\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 10.0,\n" +
                "  \"priceLabel\" : \"Was £22, now £10\"\n" +
                "}, {\n" +
                "  \"productId\" : \"6\",\n" +
                "  \"title\" : \"item 6\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"Blue\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"123\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Red\",\n" +
                "    \"rgbColor\" : \"FF0000\",\n" +
                "    \"skuid\" : \"456\"\n" +
                "  }, {\n" +
                "    \"color\" : \"Green\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"789\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 10.0,\n" +
                "  \"priceLabel\" : \"Was £222, now £10\"\n" +
                "} ]";

        assertEquals(expected, result);
    }
}
