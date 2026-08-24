package com.cbec.common.exception;

import com.cbec.common.Result;
import com.cbec.common.constant.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        log.error("业务异常 | traceId: {} | code: {} | msg: {}", traceId, e.getCode(), e.getMsg(), e);
        return Result.error(e.getCode(), e.getMsg()).withTraceId(traceId);
    }

    /**
     * 处理 JSR-303 参数校验异常（@Valid 校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("参数校验失败 | traceId: {} | msg: {}", traceId, errorMsg, e);
        return Result.error(ResultCode.PARAM_VALID_ERROR.getCode(), errorMsg).withTraceId(traceId);
    }

    /**
     * 处理参数绑定异常（如 @RequestParam 缺失、类型转换失败）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        String errorMsg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("参数绑定失败 | traceId: {} | msg: {}", traceId, errorMsg, e);
        return Result.error(ResultCode.BAD_REQUEST.getCode(), errorMsg).withTraceId(traceId);
    }

    /**
     * 处理其他所有未捕获的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        log.error("系统异常 | traceId: {} | 异常类型: {} | msg: {}", traceId, e.getClass().getSimpleName(), e.getMessage(), e);
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMsg()).withTraceId(traceId);
    }
    /**
     * 处理请求方法不匹配异常（如用 POST 访问 GET 接口）
     */
    /**
     * 处理请求方法不匹配异常（如用 POST 访问 GET 接口）
     */
    /**
     * 处理请求方法不匹配异常（如用 POST 访问 GET 接口）
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        log.error("请求方法不匹配 | traceId: {} | 实际方法: {} | 支持的方法: {}",
                traceId, e.getMethod(), String.join(", ", e.getSupportedMethods()));
        return Result.error(405, "请求方法不支持，请检查使用正确的HTTP方法（" + String.join(", ", e.getSupportedMethods()) + "）")
                .withTraceId(traceId);
    }

    /**
     * 处理 Content-Type 不匹配异常（如用表单提交访问 JSON 接口）
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<?> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        log.error("Content-Type 不匹配 | traceId: {} | 实际: {} | 支持: {}",
                traceId, e.getContentType(), e.getSupportedMediaTypes());
        return Result.error(415, "请求的 Content-Type 不支持，请使用 application/json")
                .withTraceId(traceId);
    }
}