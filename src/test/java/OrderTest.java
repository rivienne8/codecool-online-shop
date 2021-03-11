import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderTest {

    // class to be tested
    private Order order;

    //Dependencies
    private Cart cart;
    private Payment payment;

    @BeforeEach
    void setup(){

        cart = mock(Cart.class);
        order = new Order(cart);
    }

    @Test
    void givenOrder_whenGetCartSize_return4(){
        int expectedSize = 4;
        when(cart.getSize()).thenReturn(expectedSize);

        int returnedSize = order.getCart().getSize();

        assertEquals(expectedSize,returnedSize);
    }

}
