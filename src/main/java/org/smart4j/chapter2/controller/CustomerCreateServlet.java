package org.smart4j.chapter2.controller;

import org.smart4j.chapter2.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/customer_create")
public class CustomerCreateServlet extends HttpServlet {
    private CustomerService service;

    @Override
    public void init() throws ServletException {
        service = new CustomerService();
    }

    //进入创建客户页面
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO
    }

    //处理创建客户请求
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO
    }
}
