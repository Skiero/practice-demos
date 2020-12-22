package com.hikvision.fireprotection.alarm.common.config.exception;

import com.hikvision.fireprotection.alarm.common.enums.BusinessExceptionEnum;
import com.hikvision.fireprotection.alarm.common.exception.BusinessException;
import com.hikvision.fireprotection.alarm.model.vo.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GlobalExceptionConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:15
 * @since 1.0.100
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandleConfiguration {

    @ExceptionHandler
    public ServerResponse<?> handle(ConstraintViolationException e) {
        log.error("The global exception handler caught a get method valid exception.", e);
        ServerResponse<?> serverResponse = ServerResponse.error(BusinessExceptionEnum.INVALID_PARAMETER);

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        if (violations != null && !violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            serverResponse.setMsg(message);
        }

        return serverResponse;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ServerResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("The global exception handler caught a post method valid exception.", e);
        ServerResponse<?> serverResponse = new ServerResponse<>();

        String defaultMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        serverResponse.setCode(BusinessExceptionEnum.INVALID_PARAMETER.getCode());
        serverResponse.setMsg(defaultMessage);

        return serverResponse;
    }

    @ExceptionHandler(value = BusinessException.class)
    public ServerResponse<?> handleBusinessException(BusinessException e) {
        log.error("The global exception handler caught a business exception.", e);
        return new ServerResponse<>(e);
    }

    @ExceptionHandler(value = Exception.class)
    public ServerResponse<?> handleException(Exception e) {
        log.error("The global exception handler caught an exception.", e);
        return new ServerResponse<>(BusinessExceptionEnum.SYSTEM_ERROR);
    }
}
