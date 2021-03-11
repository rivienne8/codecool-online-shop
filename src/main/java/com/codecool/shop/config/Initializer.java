package com.codecool.shop.config;

import com.codecool.shop.dao.PaymentProviderDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Payment;
import com.codecool.shop.util.DaoNames;
import com.codecool.shop.util.PropertiesReader;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {
    private ManagerDaoJdbc managerDaoJdbc;

    @Override
    public void contextInitialized(ServletContextEvent sce) {


        PaymentProviderDao paymentDataStore = PaymentProviderDaoMem.getInstance();

        ManagerDaoJdbc.getInstance();

        //setting up payments methods
        paymentDataStore.add(new Payment("PayPal"));
        paymentDataStore.add(new Payment("Credit Card"));
    }

}
