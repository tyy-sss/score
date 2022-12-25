package com.example.go.Test;

import com.example.go.service.order.OrderService;
import com.example.go.service.power.MenuPowerService;
import com.example.go.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuPowerService menuPowerService;

    @Autowired
    public OrderService orderService;
    @Test
    public void Test1(){
        //System.out.println(userService.emailIsExists("3127023395@qq.com"));
//        menuPowerService.getMenu(0);
        System.out.println(orderService.checkAllOrderByManager(1, 10, "2"));
    }
}
