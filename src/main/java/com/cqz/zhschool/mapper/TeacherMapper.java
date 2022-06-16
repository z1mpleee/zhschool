package com.cqz.zhschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqz.zhschool.pojo.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherMapper extends BaseMapper<Teacher> {

    Teacher selectAllByNameAndPassword(String name, String password);
}
