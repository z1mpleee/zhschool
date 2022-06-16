package com.cqz.zhschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.zhschool.pojo.Student;
import com.cqz.zhschool.service.StudentService;
import com.cqz.zhschool.util.MD5;
import com.cqz.zhschool.util.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    final private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "clazzName", required = false) String clazzName
    ) {
        Page<Student> pageParam = new Page<>(pageNo, pageSize);
        Page<Student> page = studentService.getStudentByOpr(pageParam, name, clazzName);

        return Result.ok(page);
    }

    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student) {
        if (null == student.getId() || "".equals(student.getId())) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        boolean success = studentService.saveOrUpdate(student);
        if (success) {
            return Result.ok().message("成功");
        } else {
            return Result.fail().message("失败");
        }
    }

    @DeleteMapping("/delStudentById")
    public Result delStudentById(@RequestBody List<String> delIds) {
        boolean success = studentService.removeByIds(delIds);

        if (success) {
            return Result.ok().message("成功");
        } else {
            return Result.fail().message("失败");
        }
    }
}
