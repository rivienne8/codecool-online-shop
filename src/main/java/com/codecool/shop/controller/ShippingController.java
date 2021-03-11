package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementationJdbc.ManagerDaoJdbc;
import com.codecool.shop.model.Customer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/shipping/save"})
public class ShippingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("cart", session.getAttribute("cart"));
        context.setVariable("shipping", false);
        engine.process("order/order.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();
        Customer customerShippingInfo = new Customer(req.getParameter("firstName"), req.getParameter("lastName"),
                req.getParameter("email"), req.getParameter("city"), req.getParameter("country"),
                req.getParameter("postCode"), req.getParameter("street"), req.getParameter("phoneNumber"));

        customerShippingInfo.setId(Integer.parseInt(session.getAttribute("userId").toString()));

        ManagerDaoJdbc.getInstance().getShippingInfoDao().add(customerShippingInfo);
        resp.sendRedirect("/cart");
    }
}
