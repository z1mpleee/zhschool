package com.cqz.zhschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.zhschool.pojo.Teacher;
import com.cqz.zhschool.service.TeacherService;
import com.cqz.zhschool.util.MD5;
import com.cqz.zhschool.util.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Api
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    final private TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "name", required = false) String name

    ) {

        Page<Teacher> pageParam = new Page<>(pageNo, pageSize);
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByAsc("id");

        Page<Teacher> page = teacherService.page(pageParam, queryWrapper);

        return Result.ok(page);
    }

    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher) {

        if ("".equals(teacher.getId()) || null == teacher.getId()) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        boolean success = teacherService.saveOrUpdate(teacher);

        if (success) {
            return Result.ok().message("成功.");
        } else {
            return Result.fail().message("失败.");
        }
    }

    @DeleteMapping("/deleteTeacher")
    public Result deleteAdmin(@RequestBody List<String> delIds) {

        boolean success = teacherService.removeByIds(delIds);

        if (success) {
            return Result.ok().message("成功.");
        } else {
            return Result.fail().message("失败");
        }
    }
}
