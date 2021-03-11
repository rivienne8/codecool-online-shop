package com.codecool.shop.controller;

import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(urlPatterns = {"/cart/update"})
public class CartUpdater extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(CartUpdater.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Cart cart = (Cart) session.getAttribute("cart");
        String action = req.getParameter("action");
        updateCart(cart, action, req);
        resp.sendRedirect("/cart");
    }

    private void updateCart(Cart cart, String action, HttpServletRequest req){
        int quantityBefore = cart.getQuantity();
        float totalPriceBefore = cart.getTotalPrice();

        if (action.equals("remove")){
            cart.remove(Integer.parseInt(req.getParameter("id")));
        } else if (action.equals("decrease")){
            cart.decreaseQuantity(Integer.parseInt(req.getParameter("id")));
        } else if (action.equals("increase")){
            cart.increaseQuantity(Integer.parseInt(req.getParameter("id")));
        }

        if (cart.getId() != 0){
            ManagerDaoJdbc.getInstance().getCartDao().update(cart);
        }
        logger.debug("{} from cart product id: {}, quantity after: {}, quantity before: {} " +
                ", total price after: {}, total price before: {}", action, req.getParameter("id"),
                cart.getQuantity(), quantityBefore,  cart.getTotalPrice(),totalPriceBefore );
    }
}
