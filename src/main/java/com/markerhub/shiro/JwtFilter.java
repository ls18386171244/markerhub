package com.markerhub.shiro;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.markerhub.common.lang.Result;
import com.markerhub.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.shiro
 * @ClassName: JwtFilter
 * @Author: admin
 * @Description: 定义jwt的过滤器jwtFilter
 * @Date: 2021/8/8 21:57
 * @Version: 1.0
 */

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt))
            return null;
        return new JwtToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServerRequest request = (HttpServerRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) { //表示无jwt
            return true;
        } else {
            //校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
            //执行登录
            return executeLogin(servletRequest, servletResponse);
        }
    }

    //  登录失败
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServerResponse = (HttpServletResponse) response;
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        // 将result以json格式返回
        String json = JSONUtil.toJsonStr(result);
        try {
            httpServerResponse.getWriter().println(json);  // 打印
        } catch (IOException ie) {

        }
        return false;
    }
}
