package com.cqz.zhschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.zhschool.mapper.TeacherMapper;
import com.cqz.zhschool.pojo.LoginForm;
import com.cqz.zhschool.pojo.Teacher;
import com.cqz.zhschool.service.TeacherService;
import com.cqz.zhschool.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service()
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    private final TeacherMapper teacherMapper;

    public TeacherServiceImpl(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Override
    public Teacher login(LoginForm loginForm) {
        return teacherMapper.selectAllByNameAndPassword(loginForm.getUsername(), MD5.encrypt(loginForm.getPassword()));
    }
}
