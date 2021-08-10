package com.markerhub.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.common.dto
 * @ClassName: LoginDto
 * @Author: admin
 * @Description:
 * @Date: 2021/8/10 16:56
 * @Version: 1.0
 */
@Data
public class LoginDto implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
