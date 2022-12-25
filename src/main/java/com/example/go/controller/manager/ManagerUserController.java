package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.mapper.UserMapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.User;
import com.example.go.service.user.UserService;
import com.example.go.utils.GetNowTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 管理员管理用户
 */
@Slf4j
@RestController
@RequestMapping("/managerUser")
public class ManagerUserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public R addUser(@RequestBody User user) throws ParseException {
        //条件查询email 用户
        User user1 = userService.emailIsExists(user.getEmail());
        //用户不存在
        if (user1 != null) {
            return R.error(MessageNews.MESSAGE_MANAGER_REGISTER_FAIL_EMAIL_EXIST);
        }
        //设置默认密码 123456
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        user.setPassword(password);
        //处理性别
        if (user.getSex().equals("0")){
            user.setSex("女");
        }else{
            user.setSex("男");
        }
        //设置状态是通用
        user.setStatus(1);
        //设置账号余额为0
        user.setAccount(0);
        //设置创建时间
        user.setCreateTime(GetNowTime.getNowTime());
        //注册账号
        userMapper.insert(user);
        return R.success(MessageNews.MESSAGE_MANAGER_ADD_USER_SUCCESS);
    }

    @PostMapping("/changeUser")
    public R changeUser(@RequestBody User user){
        System.out.println(user);
        //条件查询email 用户
        User user1 = userService.emailIsExists(user.getEmail());
        String sex = "男";
        if(user.getSex().equals(0)){
            sex = "女";
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(User::getName,user.getName())
                .set(User::getAge,user.getAge())
                .set(User::getSex,sex)
                .set(User::getBirthday,user.getBirthday())
                .eq(User::getId,user1.getId());
        int rows = userMapper.update(null,updateWrapper);
        if(rows != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_USER_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_USER_SUCCESS);
    }

    //禁用用户
    @PostMapping("/stopUser")
    public R stopUser(@RequestBody User user){
        System.out.println(user);
        //条件查询email 用户
        User user1 = userService.emailIsExists(user.getEmail());
        Integer status = 0;
        if( user.getStatus() == 0 ){
            status = 1;
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(User::getStatus,status)
                .eq(User::getId,user1.getId());
        int rows = userMapper.update(null,updateWrapper);
        if(rows != 1){
            return R.error(MessageNews.MESSAGE_MANAGER_CHANGE_USER_STATUS_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_CHANGE_USER_STATUS_SUCCESS);
    }

    @DeleteMapping("/deleteUser/{email}")
    public R deleteUser(@PathVariable("email") String email){
        //条件查询email 用户
        User user1 = userService.emailIsExists(email);
        System.out.println(user1.getId());
        int i = userMapper.deleteById(user1.getId());
        if( i == 0 ){
            return R.error(MessageNews.MESSAGE_MANAGER_DELETE_USER_FAIL);
        }
        return R.success(MessageNews.MESSAGE_MANAGER_DELETE_USER_SUCCESS);
    }
}
