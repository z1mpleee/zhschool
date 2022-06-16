package com.cqz.zhschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.zhschool.pojo.Admin;
import com.cqz.zhschool.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    public Admin login(LoginForm loginForm);
}
