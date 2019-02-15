package org.smart4j.chapter2.service;

import org.smart4j.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

//提供客户数据服务
public class CustomerService {
    //获取客户列表
    public List<Customer> getCustomerList(String keyword){
        //TODO
        return null;
    }

    //查询客户信息
    public Customer getCustomer(long id){
        //TODO
        return null;
    }

    //创建客户信息
    public boolean createCustomer(Map<String,Object> fieldMap){
        //TODO
        return false;
    }

    //更新客户信息
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        //TODO
        return false;
    }

    //删除客户信息
    public boolean deleteCustomer(long id){
        //TODO
        return false;
    }
}
