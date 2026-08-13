package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.common.utils.JwtUtils;
import com.cbec.common.utils.PasswordEncoder;
import com.cbec.entity.SysUser;
import com.cbec.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/auth/login")
    public Result<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password) {

        // 1. 根据用户名查询用户
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 校验用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        // 4. 生成 Token
        String accessToken = jwtUtils.generateToken(username, user.getId());
        String refreshToken = jwtUtils.generateRefreshToken(username, user.getId());

        // 5. 返回结果
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());

        return Result.success(data);
    }
}