package com.markerhub.controller;


import com.markerhub.common.lang.Result;
import com.markerhub.entity.User;
import com.markerhub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;

    @GetMapping("/index")
    public Result index(){
        User user = service.getById(1L);
        return Result.success(user);
    }

}
