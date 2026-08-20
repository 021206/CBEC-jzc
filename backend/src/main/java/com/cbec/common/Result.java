package com.cbec.common;

import lombok.Data;
import org.slf4j.MDC;

@Data
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;
    private String traceId;

    // 私有构造，不允许直接 new，必须通过静态方法创建
    private Result() {}

    // ============ 成功响应 ============
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        result.setTraceId(MDC.get("traceId"));
        return result;
    }

    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        result.setTraceId(MDC.get("traceId"));
        return result;
    }

    // ============ 失败响应 ============
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        result.setTraceId(MDC.get("traceId"));
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        result.setTraceId(MDC.get("traceId"));
        return result;
    }

    // ============ 链式设置 traceId ============
    public Result<T> withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}