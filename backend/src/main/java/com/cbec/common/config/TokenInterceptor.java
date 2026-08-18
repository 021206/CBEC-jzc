package com.cbec.common.config;

import com.cbec.common.Result;
import com.cbec.common.utils.JwtUtils;
import com.cbec.common.utils.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.MDC;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    // 白名单：不需要 Token 的接口路径
    private static final String[] WHITE_LIST = {
            "/auth/login",
            "/auth/refresh",
            "/hello",
            "/test/error",
            "/auth/menus"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 生成 traceId（如果有 MDC 的话）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
            return true; // 放行
        }
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put("traceId", traceId);

        String path = request.getRequestURI();
        System.out.println("========== 拦截器拦截到请求: " + path + " ==========");  // ← 加这一行

        // 1. 白名单放行
        for (String white : WHITE_LIST) {
            if (path.startsWith(white)) {
                System.out.println("白名单放行: " + path);
                return true;
            }
        }

        // 2. 从请求头获取 Token
        String authorization = request.getHeader("Authorization");
        System.out.println("Authorization 头: " + authorization);  // ← 加这一行

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("Token 缺失或格式错误");
            writeErrorResponse(response, 401, "未登录，请先登录");
            return false;
        }

        String token = authorization.substring(7);
        System.out.println("提取的 Token (前50字符): " + token.substring(0, Math.min(token.length(), 50)));  // ← 加这一行

        // 3. 校验 Token 是否有效
        if (!jwtUtils.validateToken(token)) {
            System.out.println("Token 无效或已过期");
            writeErrorResponse(response, 401, "Token 无效或已过期，请重新登录");
            return false;
        }

        // 4. 解析 Token 获取用户 ID
        Long userId = jwtUtils.getUserIdFromToken(token);
        System.out.println("解析出的用户 ID: " + userId);  // ← 加这一行

        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后清理 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }

    /**
     * 返回统一格式的错误响应（不走全局异常，因为还没到 Controller）
     */
    private void writeErrorResponse(HttpServletResponse response, int code, String msg) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // 允许前端端口
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setStatus(200); // 前端统一根据 code 判断，所以 http status 还是 200
        response.setContentType("application/json;charset=utf-8");

        Result<Object> result = Result.error(code, msg);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);

        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
        out.close();
    }
}