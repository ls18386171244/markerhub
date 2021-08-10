package com.markerhub.util;

import com.markerhub.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.util
 * @ClassName: ShiroUtils
 * @Author: admin
 * @Description:
 * @Date: 2021/8/10 18:14
 * @Version: 1.0
 */
public class ShiroUtils {
    /*
    * 获取shio中登录的用户信息
    * */
    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
