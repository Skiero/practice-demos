package com.hikvision.fireprotection.hikalarm.common.config;

import com.hikvision.fireprotection.hikalarm.common.enums.BusinessExceptionEnum;
import com.hikvision.fireprotection.hikalarm.common.exception.BusinessException;
import com.hikvision.fireprotection.hikalarm.model.vo.ServerResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * GlobalExceptionConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:15
 * @since 1.0.100
 */
@RestControllerAdvice
public class ExceptionHandleConfiguration {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ServerResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        ServerResponse<?> serverResponse = new ServerResponse<>();
        serverResponse.setCode(BusinessExceptionEnum.SYSTEM_ERROR.getCode());
        serverResponse.setMsg(defaultMessage);
        return serverResponse;
    }

    @ExceptionHandler(value = BusinessException.class)
    public ServerResponse<?> handleBusinessException(BusinessException e) {
        ServerResponse<?> serverResponse = new ServerResponse<>(e);
        return serverResponse;
    }

    @ExceptionHandler(value = Exception.class)
    public ServerResponse<?> handleException(Exception e) {
        ServerResponse<?> serverResponse = new ServerResponse<>(BusinessExceptionEnum.SYSTEM_ERROR);
        return serverResponse;
    }
}
