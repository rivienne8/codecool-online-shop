package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
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

@WebServlet(urlPatterns = {"/cart/", "/cart"})
public class CartController extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(CartController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Cart cart = (Cart) session.getAttribute("cart");

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = setContextVariables(req,resp,cart, session);

        logger.debug("Displaying cart, quantity: {}, total price: {}", cart.getQuantity(), cart.getTotalPrice());

        engine.process("cart/cartView.html", context, resp.getWriter());
    }

    private WebContext setContextVariables(HttpServletRequest req, HttpServletResponse resp,
                                           Cart cart,
                                           HttpSession session){
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("productsLines", cart.getProductLines());
        context.setVariable("cart", cart);
        if (req.getParameter("message")!= null){
            context.setVariable("message", req.getParameter("message"));
        }
        if (notUserLogged(session)){
            context.setVariable("isLoggedIn", false);
        } else {
            context.setVariable("isLoggedIn", true);
        }
        return context;
    }


    private boolean notUserLogged(HttpSession session){
        return session.getAttribute("userId") == null;
    }

}


