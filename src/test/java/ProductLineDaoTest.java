import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementationJdbc.ProductDaoJdbc;
import com.codecool.shop.dao.implementationJdbc.ProductLineDaoJdbc;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductLineDaoTest {

    private DataSource ds;
    private Connection c;
    private PreparedStatement stmt;
    private ResultSet rs;

    private ProductDao productDao;
    private Product product;

    private ProductLine productLine;

    @BeforeEach
    public void setUp() throws Exception {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        productDao = mock(ProductDaoJdbc.class);
        product = mock(Product.class);

        assertNotNull(ds);

        when(product.getId()).thenReturn(2);
        when(productDao.find(2)).thenReturn(product);
        when(c.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(c.prepareStatement(any(String.class))).thenReturn(stmt);
        when(ds.getConnection()).thenReturn(c);

        productLine = new ProductLine(product);

        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(22);
        when(rs.getInt("id")).thenReturn(22);
        when(rs.getInt("product_id")).thenReturn(2);
        when(rs.getInt("quantity")).thenReturn(productLine.getQuantity());
        when(stmt.executeQuery()).thenReturn(rs);
        when(stmt.getGeneratedKeys()).thenReturn(rs);
    }


    @Test
    public void givenProductLine_whenProductLineDaoAdd_returnWithIdAndSetIdToProductLine() throws SQLException {

        new ProductLineDaoJdbc(ds, productDao).add(productLine, 3);
        int expectedId = 22;
        assertEquals(expectedId,productLine.getId());
    }

    @Test
    public void addAndFindGivenProductLine() throws Exception {

        ProductLineDaoJdbc dao = new ProductLineDaoJdbc(ds, productDao);
        dao.add(productLine, 3);
        ProductLine returnedProdLine = dao.find(22);

        assertEquals(productLine, returnedProdLine);
        assertEquals(productLine.getId(), returnedProdLine.getId());
        assertEquals(productLine.getQuantity(), returnedProdLine.getQuantity());
        assertEquals(productLine.getTotalPrice(), returnedProdLine.getTotalPrice());
        assertEquals(productLine.getProduct(), returnedProdLine.getProduct());

    }

}
