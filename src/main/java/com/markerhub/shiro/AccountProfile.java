package com.markerhub.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.shiro
 * @ClassName: AccountProfile
 * @Author: admin
 * @Description:
 * @Date: 2021/8/8 23:43
 * @Version: 1.0
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;
}
