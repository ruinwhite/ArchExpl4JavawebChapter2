import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {
    private final CustomerService service;

    public CustomerServiceTest(){
        service = new CustomerService();
    }

    /**
     * 初始化
     */
    @Before
    public void init(){
        String fileName = "sql/customer_init.sql";
        DatabaseHelper.executeSqlFile(fileName);
    }

    @Test
    public void getCustomerListTest(){
        List<Customer> customerList = service.getCustomerList();
        Assert.assertEquals(2,customerList.size());
    }

    @Test
    public void getCustomerTest(){
        Customer customer = service.getCustomer(1);
        Assert.assertNull(customer);
    }

    @Test
    public void createCustomerTest(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name","customer3");
        map.put("contact","ruin");
        map.put("telephone","18399763563");
        boolean result = service.createCustomer(map);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest(){
        long custId = 3L;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("telephone","18324466906");
        boolean result = service.updateCustomer(custId,map);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest(){
        long custId = 3L;
        boolean result = service.deleteCustomer(custId);
        Assert.assertTrue(result);
    }
}
