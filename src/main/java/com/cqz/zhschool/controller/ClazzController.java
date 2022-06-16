package com.cqz.zhschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.zhschool.pojo.Clazz;
import com.cqz.zhschool.service.ClazzService;
import com.cqz.zhschool.util.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "gradeName", required = false) String gradeName,
            @RequestParam(value = "name", required = false) String name
    ) {
        // 分页带条件查询
        Page<Clazz> page = clazzService.getClazzsByOpr(new Page<>(pageNo, pageSize), gradeName, name);

        return Result.ok(page);
    }

    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@RequestBody Clazz clazz) {
        boolean success = clazzService.saveOrUpdate(clazz);

        if (success) {
            return Result.ok().message("成功");
        } else {
            return  Result.fail().message("失败");
        }
    }

    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(@RequestBody List<String> delIds) {
        boolean success = clazzService.removeByIds(delIds);
        if (success) {
            return Result.ok().message("成功");
        } else {
            return  Result.fail().message("失败");
        }
    }

    @GetMapping("/getClazzs")
    public Result getClazzs() {
        List<Clazz> clazzList = clazzService.list();
        return Result.ok(clazzList);
    }
}
