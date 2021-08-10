package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.service.IBlogService;
import com.markerhub.util.ShiroUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-08-06
 */
@RestController
public class BlogController {

    @Autowired
    private IBlogService blogService;

    @GetMapping("/blogs")
    public Result queryList(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result queryOne(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        //做判断
        Assert.isNull(blog, "该博客已被删除");
        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        // 先进行一个查询，如果查询结果为空，表示新增；如果不为空，表示编辑
        Blog tmp = null;
        if (blog.getId() != null) {
            tmp = blogService.getById(blog.getId());
            //  判断有无编辑权限，当id相等时才可以编辑
            Assert.isTrue(tmp.getId() == (ShiroUtils.getProfile().getId()), "很抱歉，暂无编辑权限");
        } else {
            tmp = new Blog();
            tmp.setUserId(ShiroUtils.getProfile().getId());
            tmp.setCreated(LocalDateTime.now());
            tmp.setStatus(0);
        }
        BeanUtil.copyProperties(blog, tmp, "id", "userId", "created", "status");
        blogService.save(tmp);
        return Result.success(null);
    }


}
