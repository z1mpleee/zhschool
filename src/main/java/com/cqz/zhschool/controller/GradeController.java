package com.cqz.zhschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.zhschool.pojo.Grade;
import com.cqz.zhschool.service.GradeService;
import com.cqz.zhschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api()
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @ApiOperation("")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "gradeName", required = false) String gradeName
            ) {

        // 分页，带条件查询
        Page<Grade> pageParam = new Page<>(pageNo, pageSize);
        Page<Grade> page = gradeService.getAllGradesByOpr(pageParam, gradeName);

        //返回Result
        return Result.ok(page);
    }

    @ApiOperation("")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@RequestBody Grade grade) {
        boolean success = gradeService.saveOrUpdate(grade);

        if (success) {
            return Result.ok();
        } else {
            return Result.fail().message("失败");
        }
    }

    @ApiOperation("")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@RequestBody List<String> delIds) {
        boolean success = gradeService.removeByIds(delIds);

        if (success) {
            return Result.ok();
        } else {
            return Result.fail().message("删除失败");
        }
    }

    @GetMapping("/getGrades")
    public Result getGrades() {
        return Result.ok(gradeService.list());
    }
}
