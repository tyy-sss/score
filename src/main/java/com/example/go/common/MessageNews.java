package com.example.go.common;

public interface MessageNews {
    //用户登录
    String MESSAGE_USER_LOGIN_FAIL_EMAIL_WRONG="邮箱错误";
    String MESSAGE_USER_LOGIN_FAIL_PASSWORD_WRONG="密码错误";
    String MESSAGE_USER_LOGIN_FAIL_STATUS_WRONG="账号已禁用";
    //用户注册
    String MESSAGE_USER_REGISTER_FAIL_EMAIL_EXIST="邮箱已经注册";
    String MESSAGE_USER_REGISTER_FAIL_EMAIL_NOT_EXIST="邮箱没有注册";
    //发送验证码
    String MESSAGE_EMAIL_CODE_SEND_SENT="邮箱验证码已发送";
    //验证码过期
    String MESSAGE_EMAIL_CODE_OVERTIME="邮箱验证码过期";
    //验证码错误
    String MESSAGE_EMAIL_CODE_WRONG="验证码错误";
    //修改密码成功
    String  MESSAGE_CHANGE_PASSWORD_SUCCESS="修改密码成功";
    //修改密码失败
    String  MESSAGE_CHANGE_PASSWORD_FAIL="修改密码失败";
    //下载文件为空
    String  MESSAGE_FAIL_UPLOAD_FAIL="文件上传失败，文件为空";
}
