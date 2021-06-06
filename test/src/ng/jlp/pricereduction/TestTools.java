package ng.jlp.pricereduction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.jlp.pricereduction.data.ColorSwatch;
import ng.jlp.pricereduction.data.Price;
import ng.jlp.pricereduction.data.Product;

import java.util.ArrayList;

import static ng.jlp.pricereduction.PriceReductionService.SHOW_WAS_NOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestTools {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Data object test data creation methods

    public static ArrayList<ColorSwatch> createColorSwatches() {
        ArrayList<ColorSwatch> colorSwatches = new ArrayList<>();
        colorSwatches.add(new ColorSwatch("Blue", "Blue", "123"));
        colorSwatches.add(new ColorSwatch("Red", "RED", "456"));
        colorSwatches.add(new ColorSwatch("Green", "green", "789"));
        return colorSwatches;
    }

    public static Price createPriceWithWasNow() {
        return new Price(20.00, 0, 0, 10.00, "GBP");
    }

    public static Price createPriceNoWas() {
        return new Price(0, 0, 0, 10.00, "GBP");
    }

    public static Price createPriceNoNow() {
        return new Price(20.00, 0, 0, 0, "GBP");
    }

    public static Price createPriceWasLessThanNow() {
        return new Price(20.00, 0, 0, 21.00, "GBP");
    }

    public static Price createPriceWasSameAsNow() {
        return new Price(20.00, 0, 0, 20.00, "GBP");
    }

    public static Price createPriceWithWasNowThen1Then2() {
        return new Price(24.99, 19.99, 12.00, 10.00, "GBP");
    }

    public static Price createPriceWithWasNowThen1() {
        return new Price(22.99, 14.99, 0, 10.00, "GBP");
    }

    public static Price createPriceWithWasNowThen2() {
        return new Price(21.50, 0, 12.50, 10.00, "GBP");
    }

    public static Price createPriceWithHugeReduction() {
        return new Price(221.50, 0, 50, 10.00, "GBP");
    }

    public static Price createPriceWithTinyReduction() {
        return new Price(11, 0, 0, 10.00, "GBP");
    }

    public static ArrayList<Product> createProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Product product1 = new Product("1", "item 1", createColorSwatches(), createPriceNoWas());
        Product product2 = new Product("2", "item 2", createColorSwatches(), createPriceWithWasNowThen1());
        Product product3 = new Product("3", "item 3", createColorSwatches(), createPriceNoWas());
        Product product4 = new Product("4", "item 4", createColorSwatches(), createPriceWithWasNow());
        Product product5 = new Product("5", "item 5", createColorSwatches(), createPriceNoNow());
        Product product6 = new Product("6", "item 6", createColorSwatches(), createPriceWithTinyReduction());
        Product product7 = new Product("7", "item 7", createColorSwatches(), createPriceWithWasNowThen2());
        Product product8 = new Product("8", "item 8", createColorSwatches(), createPriceWithHugeReduction());

        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);
        products.add(product6);
        products.add(product7);
        products.add(product8);

        return products;
    }

    public static ArrayList<Product> createReducedProducts() {
        PriceReductionService service = new PriceReductionService();
        ArrayList<Product> products = new ArrayList<>();
        Product product1 = new Product("1", "item 1", createColorSwatches(), createPriceWithWasNow());
        Product product2 = new Product("2", "item 2", createColorSwatches(), createPriceWithWasNowThen1());
        Product product3 = new Product("3", "item 3", createColorSwatches(), createPriceWithTinyReduction());
        Product product4 = new Product("4", "item 4", createColorSwatches(), createPriceWithWasNowThen1Then2());
        Product product5 = new Product("5", "item 5", createColorSwatches(), createPriceWithWasNowThen2());
        Product product6 = new Product("6", "item 6", createColorSwatches(), createPriceWithHugeReduction());

        service.setPriceReductionInfo(product1, SHOW_WAS_NOW);
        service.setPriceReductionInfo(product2, SHOW_WAS_NOW);
        service.setPriceReductionInfo(product3, SHOW_WAS_NOW);
        service.setPriceReductionInfo(product4, SHOW_WAS_NOW);
        service.setPriceReductionInfo(product5, SHOW_WAS_NOW);
        service.setPriceReductionInfo(product6, SHOW_WAS_NOW);

        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);
        products.add(product6);

        return products;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Json input test data creation methods

    private static String jsonProperty(String field, String value) {
        return "\"" + field + "\": \"" + value + "\"";
    }

    private static String jsonPrice(String was, String then1, String then2, String now, String currency) {
        return "\"price\": {" +
                jsonProperty("was", was) + ", " +
                jsonProperty("then1", then1) + ", " +
                jsonProperty("then2", then2) + "," +
                jsonProperty("now", now) + ", " +
                jsonProperty("currency", currency) + " }";
    }

    private static String jsonPriceFromTo(String was, String then1, String then2, String from, String to, String currency) {
        return "\"price\": { " +
                jsonProperty("was", was) + ", " +
                jsonProperty("then1", then1) + ", " +
                jsonProperty("then2", then2) + ", \"now\": {" +
                jsonProperty("from", from) + ", " +
                jsonProperty("to", to) + " }, " +
                jsonProperty("currency", currency) + " }";
    }

    private static String jsonColorSwatch(String color, String basicColor, String skuId) {
        return "{ " +
                jsonProperty("color", color) + ", " +
                jsonProperty("basicColor", basicColor) + ", " +
                jsonProperty("skuId", skuId) + " }";
    }

    private static JsonNode createJsonNode(String input) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(input);

        return node;
    }

    private static String jsonProduct(String productId, String title, String price, String swatch) {
        return "    {\n" +
                "      " + jsonProperty("productId", productId) + ",\n" +
                "      " + jsonProperty("title", title) + ",\n" +
                "      " + price + ",\n" +
                "      \"colorSwatches\": [\n" +
                "        " + swatch + "\n" +
                "      ]\n" +
                "    }";
    }

    public static JsonNode createJsonProduct_oneColorSwatch_basicNowPrice() throws JsonProcessingException {
        String input = jsonProduct("765160",
                "John Lewis & Partners Gingham Cotton School Summer Dress",
                jsonPrice("129.00", "101.99", "", "89.00", "GBP"),
                jsonColorSwatch("Red", "Red", "236642844"));

        return createJsonNode(input);
    }

    public static JsonNode createJsonProduct_multipleColorSwatches_basicNowPrice() throws JsonProcessingException {
        String input = "{\n" +
                "  " + jsonProperty("productId", "765160") + ",\n" +
                "  " + jsonProperty("title", "John Lewis & Partners Gingham Cotton School Summer Dress") + ",\n" +
                "  " + jsonPrice("129.00", "101.99", "", "89.00", "GBP") + ",\n" +
                "  \"colorSwatches\": [\n" +
                "    " + jsonColorSwatch("Blue", "Blue", "232556407") + ",\n" +
                "    " + jsonColorSwatch("Red", "Red", "236642844") + ",\n" +
                "    " + jsonColorSwatch("Green", "Green", "236642847") + ",\n" +
                "    " + jsonColorSwatch("Navy", "Blue", "232556408") + "\n" +
                "  ]\n" +
                "}\n";
        return createJsonNode(input);
    }

    public static JsonNode createJsonProduct_nowFromTo() throws JsonProcessingException {
        String input = "{\n" +
                "  " + jsonProperty("productId", "765160") + ",\n" +
                "  " + jsonProperty("title", "John Lewis & Partners Gingham Cotton School Summer Dress") + ",\n" +
                "  " + jsonPriceFromTo("129.00", "101.99", "", "9.00", "12.00", "GBP") + ",\n" +
                "  \"colorSwatches\": [\n" +
                "    " + jsonColorSwatch("Blue", "Blue", "232556407") + ",\n" +
                "    " + jsonColorSwatch("Red", "Red", "236642844") + "\n" +
                "  ]\n" +
                "}\n";
        return createJsonNode(input);
    }

    public static JsonNode createJsonProducts_oneProduct() throws JsonProcessingException {
        String input = "{\n" +
                "  \"products\": [\n" +
                jsonProduct("4919177",
                        "John Lewis & Partners Jersey Bandeau Dress, Blue",
                        jsonPrice("", "", "", "29.00", "GBP"),
                        jsonColorSwatch("", "Blue", "238460015")) + "\n" +
                "  ]\n" +
                "}\n";
        return createJsonNode(input);
    }

    public static JsonNode createJsonProducts_multipleProducts() throws JsonProcessingException {
        String input = "{\n" +
                "  \"products\": [\n" +
                jsonProduct("4919177",
                        "John Lewis & Partners Jersey Bandeau Dress, Blue",
                        jsonPrice("", "", "", "29.00", "GBP"),
                        jsonColorSwatch("", "Blue", "238460015")) + ",\n" +
                jsonProduct("4323134",
                        "Kin Denim Shirt Dress, Blue",
                        jsonPrice("", "", "", "85.00", "GBP"),
                        jsonColorSwatch("", "Blue", "238104109")) + ",\n" +
                jsonProduct("4957430",
                        "Whistles Alba Shift Dress, Green",
                        jsonPrice("99.00", "", "", "50.00", "GBP"),
                        jsonColorSwatch("", "Green", "238653932")) + "\n" +
                "  ]\n" +
                "}\n";
        return createJsonNode(input);
    }

    public static JsonNode createJsonProducts_multipleReducedProducts() throws JsonProcessingException {
        String input = createMultipleReducedProductsInJsonString();
        return createJsonNode(input);
    }

    public static String createMultipleReducedProductsInJsonString() {
        String input = "{\n" +
                "  \"products\": [\n" +
                jsonProduct("1",
                        "item 1",
                        jsonPrice("129.00", "", "", "89.00", "GBP"),
                        jsonColorSwatch("", "Blue", "1")) + ",\n" +
                jsonProduct("2",
                        "item 2",
                        jsonPrice("69.00", "", "", "29.00", "GBP"),
                        jsonColorSwatch("", "Blue", "2")) + ",\n" +
                jsonProduct("3",
                        "item 3",
                        jsonPrice("99.00", "", "", "85.00", "GBP"),
                        jsonColorSwatch("", "Green", "3")) + ",\n" +
                jsonProduct("4",
                        "item 4",
                        jsonPrice("", "", "", "85.00", "GBP"),
                        jsonColorSwatch("", "Blue", "4")) + ",\n" +
                jsonProduct("5",
                        "item 5",
                        jsonPrice("99.00", "", "", "", "GBP"),
                        jsonColorSwatch("", "Red", "5")) + ",\n" +
                jsonProduct("6",
                        "item 6",
                        jsonPrice("299.00", "", "", "99.00", "GBP"),
                        jsonColorSwatch("", "Purple", "6")) + ",\n" +
                jsonProduct("",
                        "item 7",
                        jsonPrice("99.00", "", "", "49.00", "GBP"),
                        jsonColorSwatch("", "Yellow", "7")) + ",\n" +
                jsonProduct("8",
                        "item 8",
                        jsonPrice("99.00", "", "", "50.00", "GBP"),
                        jsonColorSwatch("", "Green", "8")) + "\n" +
                "  ]\n" +
                "}\n";
        return input;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Assertion methods

    public static void assertColorSwatch(ColorSwatch swatch1, String color, String rgbColor, String skuid) {
        assertEquals(color, swatch1.getColor());
        assertEquals(rgbColor, swatch1.getRgbColor());
        assertEquals(skuid, swatch1.getSkuid());
    }

    public static void assertPrice(Price price, double was, double then1, double then2, double now, String currency) {
        assertEquals(was, price.getWas(), 0);
        assertEquals(then1, price.getThen1(), 0);
        assertEquals(then2, price.getThen2(), 0);
        assertEquals(now, price.getNow(), 0);
        assertEquals(currency, price.getCurrency());
    }

    public static void assertProduct(Product product, String productId, String title) {
        assertEquals(productId, product.getProductId());
        assertEquals(title, product.getTitle());

        // Validate that the other fields have not yet been set
        assertEquals(0, product.getNowPrice(), 0);
        assertEquals(0, product.getReduction(), 0);
        assertNull(product.getPriceLabel());
    }

    static void assertReducedProduct(Product product, String productId, String title, double reduction) {
        assertEquals(productId, product.getProductId());
        assertEquals(title, product.getTitle());
        assertEquals(reduction, product.getReduction(), 0.2);
    }

    static void assertReducedProductFull(Product product, String productId, String title, double reduction, double now,
                                         String priceLabel) {
        assertEquals(productId, product.getProductId());
        assertEquals(title, product.getTitle());
        assertEquals(reduction, product.getReduction(), 0.2);
        assertEquals(now, product.getNowPrice(), 0);
        assertEquals(priceLabel, product.getPriceLabel());
    }
}
