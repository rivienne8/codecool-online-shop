
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Supplier supplier1 = new Supplier("supplierFirst", "desc");
    private Supplier supplier2 = new Supplier("supplierSecond", "desc");

    ProductCategory category = new ProductCategory("tablet", "departament", "descr");

    Product prodA = new Product("productA", 2.00f, "PLN", "description", category, supplier1 );
    Product prodB = new Product("productB", 4.00f, "PLN", "description", category,supplier2 );

    ProductLine testProductLine = new ProductLine(prodA);
    ProductLine testProductLine2 = new ProductLine(prodB);

//    ProductDao productDao = ProductDaoMem.getInstance();





    @Test
    void given2ProductsLineInCart_whenGetTotalPrice_ReturnSumOfPrices(){
        prodA.setId(1);
        prodB.setId(2);
        testProductLine2.setQuantity(2);
        Cart cart = new Cart();
        cart.add(testProductLine);
        cart.add(testProductLine2);

        float expectedTotalPrice = 10f;
        float returnedTotalPrice = cart.getTotalPrice();

        assertEquals(expectedTotalPrice,returnedTotalPrice);

    }

    @Test
    void givenProductId_whenDecrease_ReturnedDecreasedQuantityInProperProductLine(){
        prodA.setId(1);
        prodB.setId(2);
        testProductLine2.setQuantity(2);
        Cart cart = new Cart();
        cart.add(testProductLine);
        cart.add(testProductLine2);

        int expectedQuantityOfProductLine2 = 1;

        cart.decreaseQuantity(2);
        int returnedQuantity = cart.getProductLines().get(testProductLine2.getProduct().getId()).getQuantity();

        assertEquals(expectedQuantityOfProductLine2, returnedQuantity);

        cart.decreaseQuantity(2);
        int expectedCartSize = 1;
        int returnedCartSize = cart.getSize();

        assertEquals(expectedCartSize,returnedCartSize);

    }


    @Test
    void givenProductId_whenRemove_ReturnedCartSizeDecreasedBy1(){
        prodA.setId(1);
        prodB.setId(2);
        testProductLine2.setQuantity(2);
        Cart cart = new Cart();
        cart.add(testProductLine);
        cart.add(testProductLine2);

        int expectedSize = 1;

        cart.remove(2);
        int returnedSize = cart.getSize();

        assertEquals(expectedSize, returnedSize);

        cart.remove(1);
        expectedSize = 0;
        returnedSize = cart.getSize();

        assertEquals(expectedSize,returnedSize);

    }

    @Test
    void givenProductId_whenIncrease_ReturnedCartSizeIncreasedBy1(){
        prodA.setId(1);
        prodB.setId(2);
        testProductLine2.setQuantity(2);
        Cart cart = new Cart();
        cart.add(testProductLine);
        cart.add(testProductLine2);

        int expectedQuantityOfTestProductLine2 = 3;

        cart.increaseQuantity(2);
        int returnedQuantity = cart.getProductLines().get(2).getQuantity();

        assertEquals(expectedQuantityOfTestProductLine2, returnedQuantity);

        cart.increaseQuantity(1);
        int expectedQuantityOfTestProductLine = 2;
        returnedQuantity = cart.getProductLines().get(1).getQuantity();

        assertEquals(expectedQuantityOfTestProductLine,returnedQuantity);

    }

}
