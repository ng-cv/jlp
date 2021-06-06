package ng.jlp.pricereduction.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

/**
 * Data holder for Product
 */
@JsonPropertyOrder({ "productId", "title", "colorSwatches", "nowPrice", "priceLabel" })
@JsonIgnoreProperties(value = { "inputPrice", "reduction" })
public class Product {

    private String productId;
    private String title;
    private ArrayList<ColorSwatch> colorSwatches;
    private Price inputPrice;
    private double nowPrice;
    private double reduction;
    private String priceLabel;

    public Product(String productId, String title, ArrayList<ColorSwatch> colorSwatches, Price inputPrice) {
        this.productId = productId;
        this.title = title;
        this.colorSwatches = colorSwatches;
        this.inputPrice = inputPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<ColorSwatch> getColorSwatches() {
        return colorSwatches;
    }

    public Price getInputPrice() {
        return inputPrice;
    }

    public double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }
}
