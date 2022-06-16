package com.cqz.zhschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.zhschool.mapper.ClazzMapper;
import com.cqz.zhschool.pojo.Clazz;
import com.cqz.zhschool.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    private final ClazzMapper clazzMapper;

    public ClazzServiceImpl(ClazzMapper clazzMapper) {
        this.clazzMapper = clazzMapper;
    }

    @Override
    public Page<Clazz> getClazzsByOpr(Page<Clazz> pageParam, String gradeName, String name) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("grade_name", gradeName);
        }
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByAsc("id");

        return clazzMapper.selectPage(pageParam, queryWrapper);
    }
}
