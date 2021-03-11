package com.codecool.shop.controller;

import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/cart/save"})
public class SaveCartController extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(SaveCartController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart.getId() ==0){
            int userId = Integer.parseInt((String) session.getAttribute("userId"));
            cart.setUserId(userId);
            ManagerDaoJdbc.getInstance().getCartDao().add(cart, userId);
        } else {
            ManagerDaoJdbc.getInstance().getCartDao().update(cart);
        }

        req.setAttribute("message", "Cart saved");
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/cart");
        dispatcher.forward(req, resp);
    }

}
