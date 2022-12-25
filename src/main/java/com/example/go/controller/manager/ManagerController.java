package com.example.go.controller.manager;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.mapper.ManagerMapper;
import com.example.go.common.MessageNews;
import com.example.go.common.R;
import com.example.go.entity.Manager;
import com.example.go.entity.Menu;
import com.example.go.service.mail.MailService;
import com.example.go.service.manager.ManagerService;
import com.example.go.service.power.MenuPowerService;
import com.example.go.utils.JwtUtils;
import com.example.go.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 管理员商家的登录，注册，忘记密码
 */
@Slf4j
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private MenuPowerService menuPowerService;

    @Autowired
    private MailService mailService;
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private MailUtils mailUtils;

    @PostMapping("/login")
    public R login(@RequestBody Manager manager){
        System.out.println(manager);
        //条件查询email 用户
        Manager manager1 = managerService.emailIsExists(manager.getEmail());
        //用户不存在
        if (manager1 == null) {
            return R.error(MessageNews.MESSAGE_MANAGER_LOGIN_FAIL_EMAIL_WRONG);
        }
        if( manager1.getDisable() == 0){
            return R.error(MessageNews.MESSAGE_MANAGER_COUNT_STOP);
        }
        //对密码进行MD5加密
        String password = DigestUtils.md5DigestAsHex(manager.getPassword().getBytes());
        //密码错误
        if (!manager1.getPassword().equals(password)) {
            return R.error(MessageNews.MESSAGE_MANAGER_LOGIN_FAIL_PASSWORD_WRONG);
        }
        //获得菜单
        List<Menu> menuList = menuPowerService.getMenu(manager1.getStatus());
        //登录后将id保存在token中
        String token = JwtUtils.createToken(manager1.getId());
        //把token和菜单存在hashMap中
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("menu",menuList);
        hashMap.put("token",token);
        hashMap.put("manager",manager1);
        return R.success(hashMap);
    }

    @PostMapping("/register")
    public R register(@RequestBody Manager manager) {
        System.out.println(manager);
        //判断验证码是否过期和正确
        String codeIsRight = mailService.codeJudge(manager);
        if( codeIsRight != null){
            return R.error(codeIsRight);
        }
        //密码MD5加密
        String password = DigestUtils.md5DigestAsHex(manager.getPassword().getBytes());
        manager.setPassword(password);
        //设置状态是商家
        manager.setStatus(0);
        manager.setDisable(1);
        //注册账号
        managerMapper.insert(manager);
        //注册后把id保存在token中
        System.out.println(manager.getId());
        String token = JwtUtils.createToken(manager.getId());
        manager.setToken(token);
        //删除保存在Redis中的验证码
        redisTemplate.delete(manager.getEmail());
        return R.success(manager);
    }

    //判断邮箱是否已经注册
    @GetMapping("/registerSendCode/{email}")
    public R registerIsExist(@PathVariable("email") String email) {
        Manager manager = managerService.emailIsExists(email);
        if(manager != null){//邮箱已经注册
            return R.error(MessageNews.MESSAGE_MANAGER_REGISTER_FAIL_EMAIL_EXIST);
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
        Manager manager = managerService.emailIsExists(email);
        if(manager == null){//邮箱没有注册
            return R.error(MessageNews.MESSAGE_MANAGER_REGISTER_FAIL_EMAIL_NOT_EXIST);
        }else{//邮箱已经注册发送验证码
            //生成验证码
            String code = MailUtils.getCode();
            System.out.println(code);
            mailUtils.sendEmail(email,code);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return R.success(MessageNews.MESSAGE_EMAIL_CODE_SEND_SENT);
        }
    }

    //注册
    @PostMapping("/forgetPassword")
    public R forgetPassword(@RequestBody Manager manager){
        //判断验证码是否过期和正确
        String codeIsRight = mailService.codeJudge(manager);
        if( codeIsRight != null){
            return R.error(codeIsRight);
        }
        //改密码
        Manager manager1 = managerService.emailIsExists(manager.getEmail());
        String password = DigestUtils.md5DigestAsHex(manager.getPassword().getBytes());
        LambdaUpdateWrapper<Manager> updateWrapper = new LambdaUpdateWrapper<Manager>()
                .set(Manager::getPassword,password)
                .eq(Manager::getId,manager1.getId());
        int rows = managerMapper.update(null,updateWrapper);
        if(rows != 1){
            return R.error(MessageNews.MESSAGE_CHANGE_PASSWORD_FAIL);
        }
        //删除保存在Redis中的验证码
        redisTemplate.delete(manager.getEmail());
        return R.success(manager1);
    }
}
