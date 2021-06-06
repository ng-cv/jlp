package ng.jlp.pricereduction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.jlp.pricereduction.data.ColorSwatch;
import ng.jlp.pricereduction.data.Price;
import ng.jlp.pricereduction.data.Product;

import java.util.ArrayList;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

/**
 * JsonMapper class to map from Json into data objects, and map from data objects into Json.
 */
public class JsonMapper {

    /**
     * Map list of Products from the given Json root node.
     * Only the raw input data is populated in the products, and the derived fields (nowPrice, reduction and priceLabel)
     * is handled when processing the products in the PriceReductionService.
     * @param root
     * @return
     */
    static ArrayList<Product> mapProductsFromJson(JsonNode root) {
        ArrayList<Product> products = new ArrayList<>();
        JsonNode productNodes = root.path("products");

        for(JsonNode productNode : productNodes) {
            String productId = productNode.path("productId").asText();
            String title = productNode.path("title").asText();
            ArrayList<ColorSwatch> colorSwatches = mapColorSwatchesFromJson(productNode);
            Price inputPrice = mapPriceFromJson(productNode);
            Product product = new Product(productId, title, colorSwatches, inputPrice);

            products.add(product);
        }

        return products;
    }

    /**
     * Map the Price from the given Json product node.
     * Note that any price.now with '{' will default to 0, so 'from' and 'to' now prices aren't included in the selection
     * of products with reduced prices as it isn't possible to get a single reduced price & it isn't clear which one to use
     * @param productNode
     * @return
     */
    static Price mapPriceFromJson(JsonNode productNode) {
        JsonNode priceNode = productNode.path("price");
        double was = priceNode.path("was").asDouble();
        double then1 = priceNode.path("then1").asDouble();
        double then2 = priceNode.path("then2").asDouble();
        double now = priceNode.path("now").asDouble();
        String currency = priceNode.path("currency").asText();
        Price price = new Price(was, then1, then2, now, currency);

        return price;
    }

    /**
     * Map list of ColorSwatches from the give Json product node.
     * @param productNode
     * @return
     */
    static ArrayList<ColorSwatch> mapColorSwatchesFromJson(JsonNode productNode) {
        ArrayList<ColorSwatch> colorSwatches = new ArrayList<>();
        JsonNode colorSwatchNodes = productNode.path("colorSwatches");

        for(JsonNode node : colorSwatchNodes) {
            String color = node.path("color").asText();
            String basicColor = node.path("basicColor").asText();
            String skuid = node.path("skuId").asText();
            ColorSwatch swatch = new ColorSwatch(color, basicColor, skuid);
            colorSwatches.add(swatch);
        }

        return colorSwatches;
    }

    /**
     * Map a list of Products into a Json string.
     * @param products
     * @return
     */
    static String mapProductsToJson(ArrayList<Product> products) {
        try {
            return new ObjectMapper().enable(INDENT_OUTPUT).writeValueAsString(products);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
