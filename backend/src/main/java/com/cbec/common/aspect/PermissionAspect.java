package com.cbec.common.aspect;

import com.cbec.common.annotation.RequiresPermission;
import com.cbec.common.exception.BusinessException;
import com.cbec.common.utils.UserContext;
import com.cbec.service.PermissionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Set;


@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private PermissionService permissionService;

    @Around("@annotation(com.cbec.common.annotation.RequiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取当前用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException("未登录，请先登录");
        }

        // 2. 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
        if (annotation == null) {
            return joinPoint.proceed();
        }
        String requiredPerm = annotation.value();

        // 3. 查询用户权限
        Set<String> permissions = permissionService.getUserPermissions(userId);

        // 4. null 表示超级管理员，直接放行
        if (permissions == null) {
            return joinPoint.proceed();
        }

        // 5. 校验权限
        if (!permissions.contains(requiredPerm)) {
            throw new BusinessException(403, "无权限访问该接口：" + requiredPerm);
        }

        return joinPoint.proceed();
    }
}