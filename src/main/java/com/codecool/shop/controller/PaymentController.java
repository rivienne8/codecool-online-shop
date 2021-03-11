package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.PaymentProviderDao;
import com.codecool.shop.dao.implementation.PaymentProviderDaoMem;
import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Payment;
import com.codecool.shop.model.log.Log;
import com.codecool.shop.model.log.LogItemFactory;
import com.codecool.shop.model.log.LogName;
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

@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(PaymentController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PaymentProviderDao paymentDataStore = PaymentProviderDaoMem.getInstance();

        HttpSession session=req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("totalPrice", cart.getTotalPrice());
        context.setVariable("paymentProvider", paymentDataStore.find(1));//paymentproviderdao.getPayment
        context.setVariable("cart", session.getAttribute("cart"));
        engine.process("payment/payment.html", context, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String payment = req.getParameter("payment");

        HttpSession session=req.getSession();
        Order order = (Order) session.getAttribute("order");
        order.setPayment(new Payment(payment));
        ManagerDaoJdbc.getInstance().getOrderDao().update(order);
        logger.debug("Adding payment details to order id {}, method: {}", order.getId(), payment);

        Log log = (Log) session.getAttribute("log");
        log.add(LogItemFactory.create(LogName.VALIDATE_PAYMENT, order));

        resp.sendRedirect("/finish");
    }

}
