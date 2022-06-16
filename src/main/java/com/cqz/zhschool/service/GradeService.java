package com.cqz.zhschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.zhschool.pojo.Grade;

public interface GradeService extends IService<Grade> {

    public Page<Grade> getAllGradesByOpr(Page<Grade> pageParam, String gradeName);
}
