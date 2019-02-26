package org.smart4j.chapter2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

//提供客户数据服务
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    //获取客户列表
    public List<Customer> getCustomerList(){
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class,sql);
    }

    //查询客户信息
    public Customer getCustomer(long id){
        //TODO
        return null;
    }

    //创建客户信息
    public boolean createCustomer(Map<String,Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    //更新客户信息
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    //删除客户信息
    public boolean deleteCustomer(long id){
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
