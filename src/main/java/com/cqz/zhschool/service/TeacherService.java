package com.cqz.zhschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.zhschool.pojo.LoginForm;
import com.cqz.zhschool.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {

    public Teacher login(LoginForm loginForm);
}
