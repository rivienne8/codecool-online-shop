package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.LogDaoMem;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.log.Log;
import com.codecool.shop.model.log.LogItem;
import com.codecool.shop.model.log.LogItemFactory;
import com.codecool.shop.model.log.LogName;
import com.codecool.shop.util.SendEmail;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@WebServlet(urlPatterns = {"/finish"})
public class FinishController extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(FinishController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        Order order = (Order) req.getSession().getAttribute("order") ;
        Log log = (Log) req.getSession().getAttribute("log") ;

        boolean status = finishedSuccessfully();

        if (status){
            finishOrder(order, log);
            req.getSession().removeAttribute("log");
            req.getSession().removeAttribute("order");
            logger.debug("Removing from session order and log");

        } else {
            savingUnfinishedOrderDeatails(order, log);
        }

        WebContext context = setContextVariable(req, resp, cart, status);
        engine.process("finish/finish.html", context, resp.getWriter());

    }

    private boolean finishedSuccessfully(){
        Random random = new Random();
        return random.nextBoolean();
    }

    private void finishOrder(Order order, Log log ){
        SendEmail sendEmail = new SendEmail();
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        sendEmail.sendEmail(order);
        logger.info("Send order confirmation to : {} ", order.getCustomer().getEmail());
        log.add(LogItemFactory.create(LogName.FINISHED, order));
        LogDaoMem.getInstance().save(log, data);
        log.clear();

        order.getPayment().setStatus(1);
        logger.info("Payment successful, method: {}", order.getPayment().getName());
        ManagerDaoJdbc.getInstance().getOrderDao().update(order);
        ManagerDaoJdbc.getInstance().getOrderDao().save(order, data);
        order.getCart().clear();
        logger.debug("Clearing cart for order id: {}", order.getId());

    }

    private void savingUnfinishedOrderDeatails(Order order, Log log){
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        LogItem item = LogItemFactory.create(LogName.ERROR, order);
        item.setDescription("payment failed");
        log.add(item);
        logger.warn("Payment failed, user id: {}, order id {} , payment method {}",
                order.getCustomer().getId(), order.getId(), order.getPayment().getName());
        LogDaoMem.getInstance().save(log, data);

        order.getPayment().setStatus(0);
        ManagerDaoJdbc.getInstance().getOrderDao().update(order);
//        OrderDaoMem.getInstance().save(order, data);
        ManagerDaoJdbc.getInstance().getOrderDao().save(order,data);
    }

    private WebContext setContextVariable(HttpServletRequest req, HttpServletResponse resp,
            Cart cart, Boolean status){
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("cart", cart);
        context.setVariable("successful", status);

        return context;
    }
}
