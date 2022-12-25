package com.example.go.controller.manager;

import com.example.go.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/managerHome")
public class ManagerHomeController {
    @GetMapping("/getData")
    public R homeData(){
        return R.success("接收到数据");
    }
}
