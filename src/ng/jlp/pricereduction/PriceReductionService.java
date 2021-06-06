package ng.jlp.pricereduction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.jlp.pricereduction.data.Price;
import ng.jlp.pricereduction.data.Product;

import java.util.ArrayList;

/**
 * PriceReductionService to handle the steps required to read the Json data, map it into the data objects needed,
 * process them and return a sorted list of reduced products in a Json string.
 */
public class PriceReductionService {

    public static final String SHOW_WAS_NOW = "ShowWasNow";
    public static final String SHOW_WAS_THEN_NOW = "ShowWasThenNow";
    // Unsure if the spec had a typo, so allowing for both versions of this label
    public static final String SHOW_PERC_DSCOUNT = "ShowPercDscount";
    public static final String SHOW_PERC_DISCOUNT = "ShowPercDiscount";

    /**
     * Handle the request to process reduced products.
     * - Map the input Json into the data objects needed.
     * - Process the products to return valid, reduced products.
     * - Sort the list of reduced products in order of greatest reductions first.
     * - Return the list of sorted, reduced products in a Json string.
     * @param data
     * @param labelType
     * @return
     */
    public String handleRequest(String data, String labelType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(data);

            // Process the products in the input
            ArrayList<Product> reducedProducts = processReducedProducts(root, labelType);

            // Sort the reduced products...
            sortProducts(reducedProducts);

            // Get the resulting JSON output
            String output = JsonMapper.mapProductsToJson(reducedProducts);
            return output;

        } catch (JsonProcessingException e) {
            System.out.println("ERROR : Failed request");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Process the reduced products by getting all the products from the Json input and mapping them to Products, then
     * validating whether the product is a reduced price product. Return the list of valid reduced products.
     * Note that I have also elected to exclude products that do not have a productId, as this probably means we can't
     * look them up in the database for selling purposes (eg. product image, stock availability, order and fulfilment etc.)
     * @param root
     * @param labelType
     * @return
     */
    ArrayList<Product> processReducedProducts(JsonNode root, String labelType) {
        ArrayList<Product> products = JsonMapper.mapProductsFromJson(root);

        ArrayList<Product> reducedProducts = new ArrayList<>();
        for(Product product : products) {
            if(!product.getProductId().isEmpty() && isValidPrice(product.getInputPrice())){
                setPriceReductionInfo(product, labelType);
                reducedProducts.add(product);
            }
        }

        return reducedProducts;
    }

    /**
     * Derive the additional information needed for reduced products and add it to the Product.
     * @param product
     * @param labelType
     */
    void setPriceReductionInfo(Product product, String labelType) {
        product.setNowPrice(roundPrice(product.getInputPrice().getNow()));
        product.setReduction(calculateReduction(product.getInputPrice()));
        product.setPriceLabel(getPriceLabel(product.getInputPrice(), labelType));
    }

    /**
     * Sort the list of reduced products, in the order of greatest to least reductions first.
     * @param reducedProducts
     * @return
     */
    ArrayList<Product> sortProducts(ArrayList<Product> reducedProducts) {
        // The sort doesn't take into account any currency differences if the products were using different currencies
        reducedProducts.sort((l, r) -> (int)(r.getReduction() * 100) - (int)(l.getReduction() * 100));
        return reducedProducts;
    }

    /**
     * A valid price must have a price.was and a price.now value to be able to calculate a reduced price, and the
     * price.was must be greater than the price.now.
     * For products that have a 'from' and 'to' in the price.now field, it isn't possible to determine a single
     * reduced price, so I have made the decision to not include those products as being valid. They will have
     * the price.now field set to 0 in the JSON mapping by default as they would have had '{' in price.now
     * @param price
     * @return
     */
    boolean isValidPrice(Price price) {
        if(price == null || price.getWas() == 0 || price.getNow() == 0 || price.getWas() <= price.getNow()) {
            return false;
        }
        return true;
    }

    /**
     * For values >= 10, round the price to the nearest value to allow display of an integer price
     * @param price
     * @return
     */
    double roundPrice(double price) {
        if(price >= 10) {
            return Math.round(price);
        }
        return price;
    }

    /**
     * Format the price so that values below 10 will show two decimal places, and values >= 10 show no decimal places
     * @param price
     * @return
     */
    String formatPrice(double price) {
        price = roundPrice(price);
        if(price < 10) {
            return String.format("%.2f", price);
        }
        else {
            return String.format("%.0f", price);
        }
    }

    /**
     * Format the percent value so that no decimal places are shown.
     * Note that this means the percent value will be automatically rounded.
     * @param percent
     * @return
     */
    String formatPercent(double percent) {
        return String.format("%.0f", percent);
    }

    /**
     * Calculate the reduction from the Was and Now values in the Price.
     * If there isn't a Was or Now value in the Price, there is no reduction.
     * @param price
     * @return
     */
    double calculateReduction(Price price) {
        if(price.getWas() == 0 || price.getNow() == 0) {
            // Price reduction is only calculated if there is a price.was and price.now
            return 0;
        }
        // Price reduction is based on the actual price.now and price.was values, not the rounded values in the price label
        return Math.abs(price.getNow() - price.getWas());
    }

    /**
     * Get the price label depending on the label type.
     * The default is ShowWasNow if the label type is empty, or if it is not one of the valid label types.
     * For ShowPercDscount, I am unsure if the spec had a typo, so allowing for both versions of this label.
     * @param price
     * @param labelType
     * @return
     */
    String getPriceLabel(Price price, String labelType) {
        String symbol = Price.currencySymbol.getOrDefault(price.getCurrency(), "£");

        if(labelType.equals(SHOW_WAS_THEN_NOW)) {
            return getShowWasThenNowLabel(price,symbol);
        }
        // Unsure if the spec had a typo, so allowing for both versions of this label
        else if(labelType.equals(SHOW_PERC_DSCOUNT) || labelType.equals(SHOW_PERC_DISCOUNT)) {
            return getShowPercDiscount(price, symbol);
        }
        else {
            // Default to ShowWasNow label for all other label types, including "ShowWasNow", an empty string, or
            // any other label type that was sent in that doesn't match current label types
            return getShowWasNowLabel(price, symbol);
        }
    }

    /**
     * Construct the ShowWasNow label.
     * @param price
     * @param symbol
     * @return
     */
    String getShowWasNowLabel(Price price, String symbol) {
        String was = formatPrice(price.getWas());
        String now = formatPrice(price.getNow());
        return "Was " + symbol + was + ", now " + symbol + now;
    }

    /**
     * Construct the ShowWasThenNow label.
     * @param price
     * @param symbol
     * @return
     */
    String getShowWasThenNowLabel(Price price, String symbol) {
        if(price.getThen1() == 0 && price.getThen2() == 0) {
            return getShowWasNowLabel(price, symbol);
        }

        String was = formatPrice(price.getWas());
        String now = formatPrice(price.getNow());
        String then;
        if(price.getThen2() > 0) {
            then = formatPrice(price.getThen2());
        }
        else {
            then = formatPrice(price.getThen1());
        }

        return "Was " + symbol + was + ", then " + symbol + then + ", now " + symbol + now;
    }

    /**
     * Construct the ShowPercDscount label.
     * The percentage discount is calculated and the percent value will be rounded if it is a decimal so that only
     * integer percentage values will be shown.
     * @param price
     * @param symbol
     * @return
     */
    String getShowPercDiscount(Price price, String symbol) {
        double pct = 100 - (price.getNow() / price.getWas()) * 100;
        String percent = formatPercent(pct);
        String now = formatPrice(price.getNow());
        return percent + "% off - now " + symbol + now;
    }
}
