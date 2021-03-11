package com.codecool.shop.controller;

import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import org.slf4j.Logger;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.*;
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

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);
    private ManagerDaoJdbc managerDaoJdbc;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        managerDaoJdbc = ManagerDaoJdbc.getInstance();
        HttpSession session=req.getSession();


        // ONLY For test purpose:
        if (session.getAttribute("userId") == null){
            session.setAttribute("userId", "1");
        }

        setCartForSession(session);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = setContextVariables(req, resp, managerDaoJdbc.getProductDao(), managerDaoJdbc.getProductCategoryDao(), session);

        engine.process("product/index.html", context, resp.getWriter());
    }

    private void setCartForSession(HttpSession session){
        Cart cart;

        if (noCartButLoggedUser(session)) {
            int userId = Integer.parseInt((String) session.getAttribute("userId"));
            cart = managerDaoJdbc.getCartDao().findOpenByUserId(userId);
            if (cart.getSize() > 0 ){
                logger.debug("Existing cart for user id {} loaded");
            } else {
                cart = new Cart();
                logger.debug("New cart for user id {} created");
            }
            session.setAttribute("cart", cart);
        } else if (noCartInSession(session) ) {
            cart = new Cart();
            session.setAttribute("cart", cart);
            logger.debug("New cart created in session");
        }
    }

    private boolean noCartButLoggedUser(HttpSession session){
        return session.getAttribute("cart") == null && session.getAttribute("userId") != null;
    }

    private boolean noCartInSession(HttpSession session){
        return session.getAttribute("cart") == null;
    }

    private WebContext setContextVariables(HttpServletRequest req, HttpServletResponse resp,
                                           ProductDao productDataStore, ProductCategoryDao productCategoryDataStore,
                                           HttpSession session){
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("category", productCategoryDataStore.find(1));
        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        context.setVariable("cart", session.getAttribute("cart"));

        return context;
    }
}
