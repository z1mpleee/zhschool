package com.cqz.zhschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqz.zhschool.pojo.Admin;
import com.cqz.zhschool.pojo.LoginForm;
import com.cqz.zhschool.pojo.Student;
import com.cqz.zhschool.pojo.Teacher;
import com.cqz.zhschool.service.AdminService;
import com.cqz.zhschool.service.StudentService;
import com.cqz.zhschool.service.TeacherService;
import com.cqz.zhschool.util.*;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Api
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    private final AdminService adminService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public SystemController(AdminService adminService, StudentService studentService, TeacherService teacherService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpSession httpSession) {
        // 验证码校验
        String sessionVerifiCode = (String)httpSession.getAttribute("verifiCode");
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码过期了，请刷新页面");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginForm.getVerifiCode())) {
            return Result.fail().message("验证码错误，请重新填写");
        }

        // 从session域中移除现有的验证码
        httpSession.removeAttribute("verifiCode");

        // 准备一个map存放响应的数据
        HashMap<String, Object> data = new HashMap<>();
        // 分用户类型进行校验
        switch (loginForm.getUserType()) {
            case 1: {
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        data.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(data);
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
            }
            case 2: {
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        data.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(data);
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
            }
            case 3: {
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        data.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(data);
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
            }
        }

        return Result.fail().message("没有此用户");
    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        // 判断token是否过期，过期则返回响应的Result对象
        if (JwtHelper.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        // 从token中解析出用户id，用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        // 分用户类型进行处理
        HashMap<String, Object> data = new HashMap<>();
        switch (userType) {
            case 1: {
                Admin admin = adminService.getById(userId);
                data.put("userType", 1);
                data.put("user", admin);
                break;
            }
            case 2: {
                Student student = studentService.getById(userId);
                data.put("userType", 2);
                data.put("user", student);
                break;
            }
            case 3: {
                Teacher teacher = teacherService.getById(userId);
                data.put("userType", 3);
                data.put("user", teacher);
            }
        }

        return Result.ok(data);
    }

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpSession httpSession, HttpServletResponse httpServletResponse) throws IOException {
        // 获取验证码和图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        String verifiCode = CreateVerifiCodeImage.getVerifiCode();

        // 返回验证码和图片
        httpSession.setAttribute("verifiCode", verifiCode);
        ImageIO.write(verifiCodeImage, "JPEG", httpServletResponse.getOutputStream());
    }

    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile) {
        // 利用UUID生成新的文件名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String newFileName = uuid + originalFilename.substring(index);

        // 保存头像
        String portraitPath = "/Users/chenqizheng/Development/Java/zhschool/src/main/resources/public/upload/" + newFileName;
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            return Result.fail().message("上传失败");
        }

        // 返回保存的路径
        String savePath = "upload/" + newFileName;
        return Result.ok(savePath);
    }

    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd,
            @RequestHeader("token") String token
    ) {

        if (JwtHelper.isExpiration(token)) {
            return Result.fail().message("登录信息已过期，请重新登录");
        }

        Integer userId = JwtHelper.getUserId(token).intValue();
        Integer userType = JwtHelper.getUserType(token);

        switch (userType) {
            case 1: {
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("id", userId).eq("password", MD5.encrypt(oldPwd));
                Admin admin = adminService.getOne(adminQueryWrapper);

                if (admin == null) {
                    return Result.fail().message("旧密码输入错误");
                }

                admin.setPassword(MD5.encrypt(newPwd));
                adminService.saveOrUpdate(admin);
                break;
            }
            case 2: {
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("id", userId).eq("password", MD5.encrypt(oldPwd));
                Student student = studentService.getOne(studentQueryWrapper);

                if (student == null) {
                    return Result.fail().message("旧密码输入错误");
                }

                student.setPassword(MD5.encrypt(newPwd));
                studentService.saveOrUpdate(student);
                break;
            }
            case 3: {
                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("id", userId).eq("password", MD5.encrypt(oldPwd));
                Teacher teacher = teacherService.getOne(teacherQueryWrapper);

                if (teacher == null) {
                    return Result.fail().message("旧密码输入错误");
                }

                teacher.setPassword(MD5.encrypt(newPwd));
                teacherService.saveOrUpdate(teacher);
                break;
            }
        }

        return Result.ok().message("修改成功");
    }
}
