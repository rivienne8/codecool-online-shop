import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartMockitoTest {

    // Class to test
    private Cart cart;

    // Dependencies
    private ProductLine productLine;
    private Product product;
    private Map<Integer, ProductLine> productLines;


    @BeforeEach
    void setup(){
        product = mock(Product.class);
        productLine = mock(ProductLine.class);
        productLines = mock(HashMap.class);
        when(product.getId()).thenReturn(0);
        when(productLine.getProduct()).thenReturn(product);
        cart = new Cart();
        cart.setProductLines(productLines);
        cart.add(productLine);
    }


    @Test
    void givenCart_whenGetProductLinesByKey0GetName_thenReturnedTablet(){
        String expectedName = "Tablet";
        when(product.getName()).thenReturn(expectedName);

        when(productLines.get(0)).thenReturn(productLine);

        ProductLine returnedProductLine = cart.getProductLines().get(0);

        assertEquals(productLine, returnedProductLine);
        assertEquals(expectedName,returnedProductLine.getProduct().getName());
    }

}
