package com.cqz.zhschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.zhschool.mapper.StudentMapper;
import com.cqz.zhschool.pojo.LoginForm;
import com.cqz.zhschool.pojo.Student;
import com.cqz.zhschool.service.StudentService;
import com.cqz.zhschool.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service()
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }


    @Override
    public Student login(LoginForm loginForm) {
        return studentMapper.selectAllByNameAndPassword(loginForm.getUsername(), MD5.encrypt(loginForm.getPassword()));
    }

    @Override
    public Page<Student> getStudentByOpr(Page pageParam, String name, String  clazzName) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(clazzName)) {
            queryWrapper.like("clazz_name", clazzName);
        }
        queryWrapper.orderByAsc("id");
        Page page = studentMapper.selectPage(pageParam, queryWrapper);

        return page;
    }
}
