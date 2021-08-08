package com.markerhub.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.util
 * @ClassName: JwtUtils
 * @Author: admin
 * @Description: Jwt工具类
 * @Date: 2021/8/8 22:44
 * @Version: 1.0
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {
    private String secret;  //密钥
    private long expire;   //超时时间
    private String header;

    //生成jwt token
    public String generateToken(long userId) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime()+expire*1000);  //过期时间
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    //  拿到token进行校验 并获取body部分
    public Claims getClaimByToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error",e);
            return null;
        }
    }

    /*
    * token是否过期   true表示过期
    * */
    public boolean isTokenExpired(Date expiration){
        return expiration.before(new Date());
    }


}
