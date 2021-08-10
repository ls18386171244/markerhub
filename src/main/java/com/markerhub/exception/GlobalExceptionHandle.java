package com.markerhub.exception;

import com.markerhub.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ProjectName: markerhub
 * @Package: com.markerhub.exception
 * @ClassName: GlobalExceptionHandle
 * @Author: admin
 * @Description: 全局异常处理
 * @Date: 2021/8/9 20:27
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)   //401 没有权限
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException se){
        log.error("运行时异常：--------{}",se);
        return Result.fail(401,se.getMessage(),null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException re){
        log.error("运行时异常：--------{}",re);
        return Result.fail(re.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException mve){
        BindingResult bindingResult = mve.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常：--------{}",mve);
        return Result.fail(error.getDefaultMessage());
    }

}
