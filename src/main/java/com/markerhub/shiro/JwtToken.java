package com.markerhub.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.shiro
 * @ClassName: JwtToken
 * @Author: admin
 * @Description:
 * @Date: 2021/8/8 22:06
 * @Version: 1.0
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String jwt){
        this.token=jwt;

    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
