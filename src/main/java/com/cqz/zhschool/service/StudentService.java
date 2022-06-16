package com.cqz.zhschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.zhschool.pojo.LoginForm;
import com.cqz.zhschool.pojo.Student;

public interface StudentService extends IService<Student> {

    Student login(LoginForm loginForm);
    Page<Student> getStudentByOpr(Page pageParam, String name, String  clazzName);
}
