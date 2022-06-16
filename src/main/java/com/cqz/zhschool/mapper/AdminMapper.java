package com.cqz.zhschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqz.zhschool.pojo.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    Admin selectAllByNameAndPassword(String name, String password);
}
