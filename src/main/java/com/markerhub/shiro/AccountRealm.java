package com.markerhub.shiro;

import com.markerhub.entity.User;
import com.markerhub.service.IUserService;
import com.markerhub.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.shiro
 * @ClassName: AccountRealm
 * @Author: admin
 * @Description:
 * @Date: 2021/8/8 21:33
 * @Version: 1.0
 */
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //  获取用户的权限信息，封装到AuthorizationInfo中 返回给shiro
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    // 身份认证 比如登录时 用户名和密码等的校验 校验成功返回对应的信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken= (JwtToken) token;
        // 获取userId
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject(); //userId存放在sub中
        // 通过userService查询user
        User user = userService.getById(Long.valueOf(userId));// 将String类型的userId转换为long类型：Long.valueOf(userId)
        if (user==null)
            throw new UnknownAccountException("账户不存在");
        if (user.getStatus()==-1)
            throw new LockedAccountException("账户已被锁定");

        // 初始化身份信息类
        AccountProfile profile=new AccountProfile();
        BeanUtils.copyProperties(user,profile);  // 复制
        /*
        *  SimpleAuthenticationInfo()
        *  参数1：身份信息（一些可以公开的信息）、参数2：密钥信息  参数3：realm名字
        * */
        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());

    }
}
