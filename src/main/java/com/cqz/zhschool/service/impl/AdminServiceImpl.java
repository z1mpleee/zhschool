package com.cqz.zhschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.zhschool.mapper.AdminMapper;
import com.cqz.zhschool.pojo.Admin;
import com.cqz.zhschool.pojo.LoginForm;
import com.cqz.zhschool.service.AdminService;
import com.cqz.zhschool.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin login(LoginForm loginForm) {
        return adminMapper.selectAllByNameAndPassword(loginForm.getUsername(), MD5.encrypt(loginForm.getPassword()));
    }
}
