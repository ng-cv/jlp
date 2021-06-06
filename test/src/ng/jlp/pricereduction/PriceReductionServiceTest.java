package ng.jlp.pricereduction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import ng.jlp.pricereduction.data.Price;
import ng.jlp.pricereduction.data.Product;
import org.junit.Test;

import java.util.ArrayList;

import static ng.jlp.pricereduction.PriceReductionService.SHOW_PERC_DISCOUNT;
import static ng.jlp.pricereduction.PriceReductionService.SHOW_PERC_DSCOUNT;
import static ng.jlp.pricereduction.PriceReductionService.SHOW_WAS_NOW;
import static ng.jlp.pricereduction.PriceReductionService.SHOW_WAS_THEN_NOW;
import static ng.jlp.pricereduction.TestTools.assertReducedProduct;
import static ng.jlp.pricereduction.TestTools.assertReducedProductFull;
import static ng.jlp.pricereduction.TestTools.createColorSwatches;
import static ng.jlp.pricereduction.TestTools.createJsonProducts_multipleReducedProducts;
import static ng.jlp.pricereduction.TestTools.createMultipleReducedProductsInJsonString;
import static ng.jlp.pricereduction.TestTools.createPriceNoNow;
import static ng.jlp.pricereduction.TestTools.createPriceNoWas;
import static ng.jlp.pricereduction.TestTools.createPriceWasLessThanNow;
import static ng.jlp.pricereduction.TestTools.createPriceWasSameAsNow;
import static ng.jlp.pricereduction.TestTools.createPriceWithHugeReduction;
import static ng.jlp.pricereduction.TestTools.createPriceWithWasNow;
import static ng.jlp.pricereduction.TestTools.createPriceWithWasNowThen1;
import static ng.jlp.pricereduction.TestTools.createPriceWithWasNowThen1Then2;
import static ng.jlp.pricereduction.TestTools.createPriceWithWasNowThen2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the PriceReductionService class.
 */
public class PriceReductionServiceTest {

    @Test
    public void testRoundPrice_simple_below10_noRounding() {
        PriceReductionService service = new PriceReductionService();

        double result = service.roundPrice(8);
        assertEquals(8, result, 0);
    }

    @Test
    public void testRoundPrice_decimal_below10_noRounding() {
        PriceReductionService service = new PriceReductionService();

        double result = service.roundPrice(9.99);
        assertEquals(9.99, result, 0);
    }

    @Test
    public void testRoundPrice_simple_above10_noRounding() {
        PriceReductionService service = new PriceReductionService();

        double result = service.roundPrice(15);
        assertEquals(15, result, 0);
    }

    @Test
    public void testRoundPrice_decimal_above10_noRounding() {
        PriceReductionService service = new PriceReductionService();

        double result = service.roundPrice(15.00);
        assertEquals(15, result, 0);
    }

    @Test
    public void testRoundPrice_decimal_above10_roundDown() {
        PriceReductionService service = new PriceReductionService();

        double result = service.roundPrice(12.20);
        assertEquals(12, result, 0);
    }

    @Test
    public void testRoundPrice_decimal_above10_roundUp() {
        PriceReductionService service = new PriceReductionService();

        double result = service.roundPrice(99.50);
        assertEquals(100, result, 0);
    }

    @Test
    public void testFormatPrice_simple_above10() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPrice(12);
        assertEquals("12", result);
    }

    @Test
    public void testFormatPrice_decimal_above10_noRounding() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPrice(12.00);
        assertEquals("12", result);
    }

    @Test
    public void testFormatPrice_decimal_above10_withRounding() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPrice(12.50);
        assertEquals("13", result);
    }

    @Test
    public void testFormatPrice_simple_below10() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPrice(2);
        assertEquals("2.00", result);
    }

    @Test
    public void testFormatPrice_decimal_below10() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPrice(2.50);
        assertEquals("2.50", result);
    }

    @Test
    public void testFormatPercent_simple_noRounding() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPercent(20);
        assertEquals("20", result);
    }

    @Test
    public void testFormatPercent_decimal_noRounding() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPercent(30.00);
        assertEquals("30", result);
    }

    @Test
    public void testFormatPercent_decimal_roundUp() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPercent(12.50);
        assertEquals("13", result);
    }

    @Test
    public void testFormatPercent_decimal_roundDown() {
        PriceReductionService service = new PriceReductionService();

        String result = service.formatPercent(12.49);
        assertEquals("12", result);
    }

    @Test
    public void testIsValidPrice_nullPrice_false() {
        PriceReductionService service = new PriceReductionService();
        assertFalse(service.isValidPrice(null));
    }

    @Test
    public void testIsValidPrice_inValidNoWas_false() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceNoWas();

        assertFalse(service.isValidPrice(price));
    }

    @Test
    public void testIsValidPrice_invalidNoNow_false() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceNoNow();

        assertFalse(service.isValidPrice(price));
    }

    @Test
    public void testIsValidPrice_invalidWasLessThanNow_false() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWasLessThanNow();

        assertFalse(service.isValidPrice(price));
    }

    @Test
    public void testIsValidPrice_invalidWasSameAsNow_false() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWasSameAsNow();

        assertFalse(service.isValidPrice(price));
    }

    @Test
    public void testIsValidPrice_valid_true() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        assertTrue(service.isValidPrice(price));
    }

    @Test
    public void testCalculateReduction_invalid_noWas() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceNoWas();

        double result = service.calculateReduction(price);
        assertEquals(0, result, 0);
    }

    @Test
    public void testCalculateReduction_invalid_noNow() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceNoNow();

        double result = service.calculateReduction(price);
        assertEquals(0, result, 0);
    }

    @Test
    public void testCalculateReduction_valid_withWasAndNow() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        double result = service.calculateReduction(price);
        assertEquals(10, result, 0);
    }

    @Test
    public void testGetShowWasNowLabel() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        String result = service.getShowWasNowLabel(price, "£");
        assertEquals("Was £20, now £10", result);
    }

    @Test
    public void testGetShowWasThenNowLabel_noThen1Then2() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        String result = service.getShowWasThenNowLabel(price, "£");
        assertEquals("Was £20, now £10", result);
    }

    @Test
    public void testGetShowWasThenNowLabel_noThen1() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen2();

        String result = service.getShowWasThenNowLabel(price, "£");
        assertEquals("Was £22, then £13, now £10", result);
    }

    @Test
    public void testGetShowWasThenNowLabel_noThen2() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getShowWasThenNowLabel(price, "£");
        assertEquals("Was £23, then £15, now £10", result);
    }

    @Test
    public void testGetShowWasThenNowLabel_bothThen1Then2() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1Then2();

        String result = service.getShowWasThenNowLabel(price, "£");
        assertEquals("Was £25, then £12, now £10", result);
    }

    @Test
    public void testGetShowPercDiscount_simplePercent() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        String result = service.getShowPercDiscount(price, "$");
        assertEquals("50% off - now $10", result);
    }

    @Test
    public void testGetShowPercDiscount_roundedPercent() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getShowPercDiscount(price, "$");
        assertEquals("57% off - now $10", result);
    }

    @Test
    public void testGetPriceLabel_showWasThenNow_noThen() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        String result = service.getPriceLabel(price, SHOW_WAS_THEN_NOW);
        assertEquals("Was £20, now £10", result);
    }

    @Test
    public void testGetPriceLabel_showWasThenNow_withThen() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getPriceLabel(price, SHOW_WAS_THEN_NOW);
        assertEquals("Was £23, then £15, now £10", result);
    }

    @Test
    public void testGetPriceLabel_showPercDscount_typo_simplePercent() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNow();

        String result = service.getPriceLabel(price, SHOW_PERC_DSCOUNT);
        assertEquals("50% off - now £10", result);
    }

    @Test
    public void testGetPriceLabel_showPercDiscount_noTypo_roundedPercent() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getPriceLabel(price, SHOW_PERC_DSCOUNT);
        assertEquals("57% off - now £10", result);
    }

    @Test
    public void testGetPriceLabel_showWasNow_validLabelType() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getPriceLabel(price, SHOW_WAS_NOW);
        assertEquals("Was £23, now £10", result);
    }

    @Test
    public void testGetPriceLabel_showWasNow_emptyLabelType() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getPriceLabel(price, "");
        assertEquals("Was £23, now £10", result);
    }

    /**
     * Test that when an unexpected label type is used, it defaults to the ShowWasNow price label format.
     */
    @Test
    public void testGetPriceLabel_showWasNow_otherLabelType() {
        PriceReductionService service = new PriceReductionService();
        Price price = createPriceWithWasNowThen1();

        String result = service.getPriceLabel(price, "Not a valid label type - default to ShowWasNow");
        assertEquals("Was £23, now £10", result);
    }

    @Test
    public void testSetPriceReductionInfo_showWasNowLabel() {
        PriceReductionService service = new PriceReductionService();
        Product product = new Product("1", "item 1", createColorSwatches(), createPriceWithHugeReduction());

        service.setPriceReductionInfo(product, SHOW_WAS_NOW);
        assertReducedProductFull(product, "1", "item 1", 211.50, 10,
                "Was £222, now £10");
    }

    @Test
    public void testSetPriceReductionInfo_showPercentLabel() {
        PriceReductionService service = new PriceReductionService();
        Product product = new Product("1", "item 1", createColorSwatches(), createPriceWithHugeReduction());

        service.setPriceReductionInfo(product, SHOW_PERC_DISCOUNT);
        assertReducedProductFull(product, "1", "item 1", 211.50, 10,
                "95% off - now £10");
    }

    @Test
    public void testSetPriceReductionInfo_showWasThenNowLabel() {
        PriceReductionService service = new PriceReductionService();
        Product product = new Product("1", "item 1", createColorSwatches(), createPriceWithHugeReduction());

        service.setPriceReductionInfo(product, SHOW_WAS_THEN_NOW);
        assertReducedProductFull(product, "1", "item 1", 211.50, 10,
                "Was £222, then £50, now £10");
    }

    /**
     * Test that the products are sorted from the greatest reduction to the least reduction.
     */
    @Test
    public void testSortProducts() {
        PriceReductionService service = new PriceReductionService();
        ArrayList<Product> products = TestTools.createReducedProducts();

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Before sorting...
        // Product 1
        Product product1 = products.get(0);
        assertReducedProduct(product1, "1", "item 1", 10);
        // Product 2
        Product product2 = products.get(1);
        assertReducedProduct(product2, "2", "item 2", 13);
        // Product 3
        Product product3 = products.get(2);
        assertReducedProduct(product3, "3", "item 3", 1);
        // Product 4
        Product product4 = products.get(3);
        assertReducedProduct(product4, "4", "item 4", 14.99);
        // Product 5
        Product product5 = products.get(4);
        assertReducedProduct(product5, "5", "item 5", 11.50);
        // Product 6
        Product product6 = products.get(5);
        assertReducedProduct(product6, "6", "item 6", 211.50);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Sort the reduced products
        service.sortProducts(products);
        assertNotNull(products);
        assertEquals(6, products.size());

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // After sorting...
        // Product 1
        product1 = products.get(0);
        assertReducedProduct(product1, "6", "item 6", 211.50);
        // Product 2
        product2 = products.get(1);
        assertReducedProduct(product2, "4", "item 4", 14.99);
        // Product 3
        product3 = products.get(2);
        assertReducedProduct(product3, "2", "item 2", 13);
        // Product 4
        product4 = products.get(3);
        assertReducedProduct(product4, "5", "item 5", 11.50);
        // Product 5
        product5 = products.get(4);
        assertReducedProduct(product5, "1", "item 1", 10);
        // Product 6
        product6 = products.get(5);
        assertReducedProduct(product6, "3", "item 3", 1);
    }

    /**
     * Test that the Json input of products is processed and returns only valid reduced products.
     * In this test, there are 8 products in the input, and 3 of them are not valid due to missing values - productId,
     * price.was and price.now.
     */
    @Test
    public void testProcessReducedProducts() {
        try {
            PriceReductionService service = new PriceReductionService();
            JsonNode products = createJsonProducts_multipleReducedProducts();

            ArrayList<Product> reducedProducts = service.processReducedProducts(products, SHOW_WAS_NOW);
            assertNotNull(reducedProducts);
            assertEquals(5, reducedProducts.size());

            // Product 1
            Product product1 = reducedProducts.get(0);
            assertReducedProduct(product1, "1", "item 1", 40);
            // Product 2
            Product product2 = reducedProducts.get(1);
            assertReducedProduct(product2, "2", "item 2", 40);
            // Product 3
            Product product3 = reducedProducts.get(2);
            assertReducedProduct(product3, "3", "item 3", 14);
            // Product 4
            Product product4 = reducedProducts.get(3);
            assertReducedProduct(product4, "6", "item 6", 200);
            // Product 5
            Product product5 = reducedProducts.get(4);
            assertReducedProduct(product5, "8", "item 8", 49);

        } catch (JsonProcessingException e) {
            fail("Failed to create JsonNode");
        }
    }

    @Test
    public void testHandleRequest_emptyInputData() {
        PriceReductionService service = new PriceReductionService();
        String input = "";

        String result = service.handleRequest(input, SHOW_WAS_NOW);
        assertEquals("[ ]", result);
    }

    /**
     * Test the overall method that takes in Json data as input, processes the products and returns a sorted list
     * of reduced products in Json format.
     */
    @Test
    public void testHandleRequest_vaidInputData() {
        PriceReductionService service = new PriceReductionService();
        String input = createMultipleReducedProductsInJsonString();

        String result = service.handleRequest(input, SHOW_WAS_NOW);
        String expected = "[ {\n" +
                "  \"productId\" : \"6\",\n" +
                "  \"title\" : \"item 6\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"\",\n" +
                "    \"rgbColor\" : \"800080\",\n" +
                "    \"skuid\" : \"6\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 99.0,\n" +
                "  \"priceLabel\" : \"Was £299, now £99\"\n" +
                "}, {\n" +
                "  \"productId\" : \"8\",\n" +
                "  \"title\" : \"item 8\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"8\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 50.0,\n" +
                "  \"priceLabel\" : \"Was £99, now £50\"\n" +
                "}, {\n" +
                "  \"productId\" : \"1\",\n" +
                "  \"title\" : \"item 1\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"1\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 89.0,\n" +
                "  \"priceLabel\" : \"Was £129, now £89\"\n" +
                "}, {\n" +
                "  \"productId\" : \"2\",\n" +
                "  \"title\" : \"item 2\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"\",\n" +
                "    \"rgbColor\" : \"0000FF\",\n" +
                "    \"skuid\" : \"2\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 29.0,\n" +
                "  \"priceLabel\" : \"Was £69, now £29\"\n" +
                "}, {\n" +
                "  \"productId\" : \"3\",\n" +
                "  \"title\" : \"item 3\",\n" +
                "  \"colorSwatches\" : [ {\n" +
                "    \"color\" : \"\",\n" +
                "    \"rgbColor\" : \"00FF00\",\n" +
                "    \"skuid\" : \"3\"\n" +
                "  } ],\n" +
                "  \"nowPrice\" : 85.0,\n" +
                "  \"priceLabel\" : \"Was £99, now £85\"\n" +
                "} ]";

        assertEquals(expected, result);
    }
}
