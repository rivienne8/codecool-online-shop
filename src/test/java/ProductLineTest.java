import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductLineTest {

    private ProductLine productLine;

    private Product product;

    @BeforeEach
    void setUp(){
        product = mock(Product.class);

    }

    @Test
    void givenMockProduct_whenGetTotalPrice_returned10(){
        float price = 5f;
        int quantity = 2;
        float expectedTotalPrice = 10f;

        when(product.getDefaultPrice()).thenReturn(price);

        productLine = new ProductLine(product,quantity);
        float returnedTotalPrice = productLine.getTotalPrice();

        assertEquals(expectedTotalPrice,returnedTotalPrice );
    }
}
