package ng.jlp.pricereduction.data;

import org.junit.Test;

import static ng.jlp.pricereduction.TestTools.assertPrice;

/**
 * Tests for the Price data holder.
 */
public class PriceTest {

    @Test
    public void testPrice() {
        Price price = new Price(21.00, 20.00, 0, 15.50, "GBP");
        assertPrice(price, 21.00, 20.00, 0, 15.50, "GBP");
    }
}
