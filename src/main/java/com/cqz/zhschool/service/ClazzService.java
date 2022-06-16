package com.cqz.zhschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.zhschool.pojo.Clazz;
import org.springframework.stereotype.Service;

public interface ClazzService extends IService<Clazz> {

    Page<Clazz> getClazzsByOpr(Page<Clazz> pageParam, String gradeName, String name);
}
