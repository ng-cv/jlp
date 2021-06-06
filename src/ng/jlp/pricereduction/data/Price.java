package ng.jlp.pricereduction.data;

import java.util.HashMap;

/**
 * Data holder for Price
 */
public class Price {
    private double was;
    private double then1;
    private double then2;
    private double now;
    private String currency;

    // This may not be needed, but allows currency options to be extended if necessary
    public static HashMap<String, String> currencySymbol;

    static {
        currencySymbol = new HashMap<>();
        currencySymbol.put("GBP", "£");
    }

    public Price(double was, double then1, double then2, double now, String currency) {
        this.was = was;
        this.then1 = then1;
        this.then2 = then2;
        this.now = now;
        this.currency = currency;
    }

    public double getWas() {
        return was;
    }

    public double getThen1() {
        return then1;
    }

    public double getThen2() {
        return then2;
    }

    public double getNow() {
        return now;
    }

    public String getCurrency() {
        return currency;
    }
}
