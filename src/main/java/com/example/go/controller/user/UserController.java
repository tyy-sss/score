package com.example.go.controller.user;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.mapper.UserMapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.User;
import com.example.go.service.mail.MailService;
import com.example.go.service.user.UserService;
import com.example.go.utils.JwtUtils;
import com.example.go.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private MailUtils mailUtils;
    //登录
    @PostMapping("/login")
    public R login(@RequestBody User user) {
        System.out.println(user);
        //条件查询email 用户
        User user1 = userService.emailIsExists(user.getEmail());
        //用户不存在
        if (user1 == null) {
            return R.error(MessageNews.MESSAGE_USER_LOGIN_FAIL_EMAIL_WRONG);
        }
        //对密码进行MD5加密
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        //密码错误
        if (!user1.getPassword().equals(password)) {
            return R.error(MessageNews.MESSAGE_USER_LOGIN_FAIL_PASSWORD_WRONG);
        }
        //查看用户状态 0是禁用状态 1是正常状态
        if (user1.getStatus() == 0) {
            return R.error(MessageNews.MESSAGE_USER_LOGIN_FAIL_STATUS_WRONG);
        }
        //登录后将id保存在token中
        String token = JwtUtils.createToken(user1.getId());
        //把token返回给前端
        user1.setToken(token);
        return R.success(user1);
    }
    //注册帐号
    @PostMapping("/register")
    public R register(@RequestBody User user) throws ParseException {
        System.out.println(user);
        //判断验证码是否过期和正确
        String codeIsRight = mailService.codeJudge(user);
        if( codeIsRight != null){
            return R.error(codeIsRight);
        }
        //注册账号
        int id = userService.insertUser(user);
        //注册后把id保存在token中
        String token = JwtUtils.createToken(id);
        user.setToken(token);
        //删除保存在Redis中的验证码
        redisTemplate.delete(user.getEmail());
        return R.success(user);
    }
    //判断邮箱是否已经注册
    @GetMapping("/registerSendCode/{email}")
    public R registerIsExist(@PathVariable("email") String email) {
        User user = userService.emailIsExists(email);
        if(user != null){//邮箱已经注册
            return R.error(MessageNews.MESSAGE_USER_REGISTER_FAIL_EMAIL_EXIST);
        }else{//邮箱没有注册发送验证码
            //生成验证码
            String code = MailUtils.getCode();
            System.out.println(code);
            mailUtils.sendEmail(email,code);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return R.success(MessageNews.MESSAGE_EMAIL_CODE_SEND_SENT);
        }
    }
    //判断邮箱是否已经注册
    @GetMapping("/forgetPasswordSendCode/{email}")
    public R forgetPasswordIsExist(@PathVariable("email") String email) {
        User user = userService.emailIsExists(email);
        if(user == null){//邮箱没有注册
            return R.error(MessageNews.MESSAGE_USER_REGISTER_FAIL_EMAIL_NOT_EXIST);
        }else{//邮箱已经注册发送验证码
            //生成验证码
            String code = MailUtils.getCode();
            System.out.println(code);
            mailUtils.sendEmail(email,code);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return R.success(MessageNews.MESSAGE_EMAIL_CODE_SEND_SENT);
        }
    }
    //修改密码
    @PostMapping("/forgetPassword")
    public R forgetPassword(@RequestBody User user){
        //判断验证码是否过期和正确
        String codeIsRight = mailService.codeJudge(user);
        if( codeIsRight != null){
            return R.error(codeIsRight);
        }
        //改密码
        User user1 = userService.emailIsExists(user.getEmail());
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(User::getPassword,password)
                .eq(User::getId,user1.getId());
        int rows = userMapper.update(null,updateWrapper);
        if(rows != 1){
            return R.error(MessageNews.MESSAGE_CHANGE_PASSWORD_FAIL);
        }
        //删除保存在Redis中的验证码
        redisTemplate.delete(user.getEmail());
        return R.success(user1);
    }

    @PostMapping("/changePerson")
    public R changePerson(@RequestBody User user){
        System.out.println(user);
        int i = userService.changePerson(user);
        return R.success(null);
    }

    @GetMapping("/getPerson/{id}")
    public R getPerson(@PathVariable("id") int id){
        User user = userService.getPerson(id);
        return R.success(user);
    }
}
