package com.cqz.zhschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.zhschool.pojo.Admin;
import com.cqz.zhschool.service.AdminService;
import com.cqz.zhschool.util.MD5;
import com.cqz.zhschool.util.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Api
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    final private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "adminName", required = false) String adminName

    ) {

        Page<Admin> pageParam = new Page<>(pageNo, pageSize);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(adminName)) {
            queryWrapper.like("admin_name", adminName);
        }
        queryWrapper.orderByAsc("id");

        Page<Admin> page = adminService.page(pageParam, queryWrapper);

        return Result.ok(page);
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin) {

        if ("".equals(admin.getId()) || null == admin.getId()) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }

        boolean success = adminService.saveOrUpdate(admin);

        if (success) {
            return Result.ok().message("成功.");
        } else {
            return Result.fail().message("失败");
        }
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<String> delIds) {

        boolean success = adminService.removeByIds(delIds);

        if (success) {
            return Result.ok().message("成功.");
        } else {
            return Result.fail().message("失败");
        }
    }
}
