package com.markerhub.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.common.dto.LoginDto;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.User;
import com.markerhub.service.IUserService;
import com.markerhub.util.JwtUtils;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.controller
 * @ClassName: AccountController
 * @Author: admin
 * @Description: 登录
 * @Date: 2021/8/10 16:54
 * @Version: 1.0
 */
@RestController
public class AccountController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){
        //通过用户名去查找用户
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        //判断用户是否存在
        Assert.notNull(user,"用户不存在");//  如果不存在，则返回message
        //判断密码是否正确
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }
        //生成token
        String token = jwtUtils.generateToken(user.getId());
        //将token存到header中，避免有时候另做延时处理
        response.setHeader("Authorization",token);
        response.setHeader("Access-Control-Expost-Headers","Authorization");
        //返回值
        return Result.success(MapUtil.builder()
                .put("id",user.getId())
                .put("username",user.getUsername())
                .put("email",user.getEmail())
                .map()   //  返回map（k-v）形式的数据
        );
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }


}
