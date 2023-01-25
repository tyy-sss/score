package com.example.go.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.go.mapper.UserMapper;
import com.example.go.entity.User;
import com.example.go.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.ParseException;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    //判断这个邮箱是否已经注册
    public User emailIsExists(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
    //注册一个用户
    public int  insertUser(User user) throws ParseException {
        //密码MD5加密
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        //设置状态是通用
        user.setStatus(1);
        //设置账号余额为0
        user.setAccount(0);
        //设置账号积分为0
        user.setScore(0);
        //设置名字
        user.setName("user");
        //设置年龄为0
        user.setAge(0);
        //设置性别
        user.setSex("男");
        //设置头像
        user.setAvatar("http://localhost:8088/api/file/644caecf4ba94f94bf65ed68b6ca0621.png");
        //设置出生日期
        user.setBirthday(GetNowTime.getNowTime());
        user.setCreateTime(GetNowTime.getNowTime());
        return userMapper.insert(user);
    }

    //更新user的最近一次上线的信息
    public int updateUpTime(int id) throws ParseException {
        User user = userMapper.selectById(id);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(User::getUpTime,GetNowTime.getNowTime())
                .eq(User::getId,user.getId());
        int update = userMapper.update(user, updateWrapper);
        return update;
    }

    //根据username找user
    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    public int changePerson(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(User::getUsername,user.getUsername())
                .set(User::getSex,user.getSex())
                .set(User::getAvatar,user.getAvatar())
                .set(User::getAge,user.getAge())
                .set(User::getBirthday,user.getBirthday())
                .eq(User::getId,user.getId());
        int update = userMapper.update(user, updateWrapper);
        return update;
    }

    public User getPerson(int id) {
        User user = userMapper.selectById(id);
        return user;
    }

    //修改用户积分
    public int saveScore(int id,double score){
        int i = userMapper.saveScore(id, score);
        return i;
    }
}
