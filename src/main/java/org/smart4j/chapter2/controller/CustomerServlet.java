package org.smart4j.chapter2.controller;

import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private CustomerService service;

    @Override
    public void init() throws ServletException {
        service = new CustomerService();
    }

    //进入客户列表界面
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList = service.getCustomerList();
        req.setAttribute("customerList",customerList);
        req.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(req, resp);
    }
}
