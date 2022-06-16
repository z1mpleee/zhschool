package com.cqz.zhschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqz.zhschool.pojo.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper extends BaseMapper<Student> {

    Student selectAllByNameAndPassword(String name, String password);
}
