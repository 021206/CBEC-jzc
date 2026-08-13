package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 1. 测试正常返回
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, CBEC! 统一响应已生效");
    }

    // 2. 测试全局异常拦截（业务异常）
    @GetMapping("/test/error")
    public Result<String> testError() {
        throw new BusinessException("这是一个测试业务异常");
    }
}