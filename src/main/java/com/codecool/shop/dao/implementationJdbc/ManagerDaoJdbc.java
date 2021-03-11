package com.codecool.shop.dao.implementationJdbc;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.util.JDBCConnection;
import com.codecool.shop.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class ManagerDaoJdbc {
    private static Logger logger = LoggerFactory.getLogger(ManagerDaoJdbc.class);
    private static ManagerDaoJdbc instance = null;

    ProductLineDao productLineDao;
    CartDao cartDao;
    OrderDao orderDao;
    ProductDao productDao;
    SupplierDao supplierDao;
    ProductCategoryDao productCategoryDao;
    UserDao userDao;
    private ShippingInfoDao shippingInfoDao;

    public static ManagerDaoJdbc getInstance(){
        if (instance == null) {
            PropertiesReader propertiesReader = new PropertiesReader();

            switch (propertiesReader.getDaoName()) {
                case JDBC:
                    instance = new ManagerDaoJdbc(new JDBCConnection().connect(propertiesReader.getUrl(),
                            propertiesReader.getPortNumber(), propertiesReader.getDatabase(),
                            propertiesReader.getUser(), propertiesReader.getPassword()));
                    break;
                case MEMORY:
                    instance = new ManagerDaoJdbc();
                    break;
            }
            logger.info("App started in {} module", propertiesReader.getDatabase());
        }
        return instance;

    }

    private ManagerDaoJdbc() {
        this.productDao = ProductDaoMem.getInstance();
        this.productCategoryDao = ProductCategoryDaoMem.getInstance();
        this.supplierDao = SupplierDaoMem.getInstance();
        memorySetUp();

    }

    private ManagerDaoJdbc(DataSource dataSource){

        this.productDao = new ProductDaoJdbc(dataSource);
        this.userDao = new UserDaoJdbc(dataSource);
        this.productCategoryDao = new ProductCategoryDaoJdbc(dataSource);
        this.productLineDao  = new ProductLineDaoJdbc(dataSource, getProductDao());
        this.cartDao = new CartDaoJdbc(dataSource, getProductLineDao());
        this.orderDao = new OrderDaoJdbc(dataSource, getCartDao());
        this.shippingInfoDao = new ShippingInfoDaoJdbc(dataSource);
    }

    public void memorySetUp() {
        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDao.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDao.add(lenovo);

        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDao.add(tablet);

        //setting up products and printing it
        productDao.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDao.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDao.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
    }

    public ProductLineDao getProductLineDao() {
        return productLineDao;
    }

    public CartDao getCartDao() {
        return cartDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public SupplierDao getSupplierDao() {
        return supplierDao;
    }

    public ProductCategoryDao getProductCategoryDao() {
        return productCategoryDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ShippingInfoDao getShippingInfoDao() {
        return shippingInfoDao;
    }
}
