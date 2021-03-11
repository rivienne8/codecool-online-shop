package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.LogDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.LogDaoMem;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Customer;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.log.Log;
import com.codecool.shop.model.log.LogItemFactory;
import com.codecool.shop.model.log.LogName;
import org.mockito.internal.matchers.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/order"})
public class CheckoutCart extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(CheckoutCart.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.getId() ==0){
            int userId = Integer.parseInt((String) session.getAttribute("userId"));
            cart.setUserId(userId);
            ManagerDaoJdbc.getInstance().getCartDao().add(cart,userId);
        }

        if (session.getAttribute("order") == null){
            Order order = createOrder(cart, session);
            Log log = createLog(order, session);
            log.add(LogItemFactory.create(LogName.CREATE,order));
            logger.debug("Created new order for cart id {}", cart.getId());
        } else {
            ManagerDaoJdbc.getInstance().getCartDao()
                    .updateOrderId(cart,((Order)session.getAttribute("order")).getId());
            logger.debug("Order updated :  cart id {}", cart.getId());
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = setContextVariables(req, resp, cart, session);
        engine.process("order/order.html", context, resp.getWriter());
    }

    private Order createOrder(Cart cart, HttpSession session){
        Order order = new Order(cart);
//        OrderDao orderDao = OrderDaoMem.getInstance();
        OrderDao orderDao = ManagerDaoJdbc.getInstance().getOrderDao();
        orderDao.add(order);
        session.setAttribute("order", order);
        logger.info("New order created - id: {}, quantity: {} , total price: {} ",
                order.getId(), order.getCart().getQuantity(), order.getCart().getTotalPrice());

        return order;
    }

    private Log createLog(Order order, HttpSession session){
        Log log = new Log();
        LogDao logDao = LogDaoMem.getInstance();
        logDao.add(log);
        session.setAttribute("log", log);
        logger.debug("Create new log for order id: {}", order.getId());

        return log;
    }


    private WebContext setContextVariables(HttpServletRequest req, HttpServletResponse resp,
                                           Cart cart, HttpSession session){
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("totalPrice", cart.getTotalPrice());
        context.setVariable("cart", cart);
        context.setVariable("shipping", true);

        if (session.getAttribute("userId") != null) {
            Customer customerShippingInfo = ManagerDaoJdbc.getInstance().getShippingInfoDao()
                    .get(Integer.parseInt(session.getAttribute("userId").toString()));
            if (customerShippingInfo != null) {
                context.setVariable("shippingInfo", customerShippingInfo);
            }
        }

        return context;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Customer customer = getCustomerAddress(req);

        HttpSession session = req.getSession();
        Order order = (Order) session.getAttribute("order");
        order.setCustomer(customer);
        order.setUserId(customer.getId());
        ManagerDaoJdbc.getInstance().getOrderDao().update(order);
        Log log = (Log) session.getAttribute("log");
        log.add(LogItemFactory.create(LogName.ADD_ADDRESS,order));

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("cart", session.getAttribute("cart"));

        engine.process("payment/payment.html", context, resp.getWriter());
    }

    private Customer getCustomerAddress(HttpServletRequest req){
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");

        String country = req.getParameter("country");
        String street = req.getParameter("street");
        String postCode = req.getParameter("postCode");
        String city = req.getParameter("city");

        Customer customer = new Customer(firstName,lastName,email,city,country,
                postCode, street, phoneNumber);

        // ONLY for test purpose
        int userId = Integer.parseInt((String) req.getSession().getAttribute("userId"));
        customer.setId(userId);

        logger.debug("Customer id {} added address information ", customer.getId());

        return customer;
    }
}



