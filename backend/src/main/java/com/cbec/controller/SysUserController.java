package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysUser;
import com.cbec.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(sysUserService.page(keyword, pageNum, pageSize));
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<SysUser> add(@RequestBody SysUser user) {
        return Result.success(sysUserService.add(user));
    }

    /**
     * 编辑用户
     */
    @PutMapping
    public Result<SysUser> update(@RequestBody SysUser user) {
        if (user.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(sysUserService.update(user));
    }

    /**
     * 切换状态
     */
    @PutMapping("/status/{id}")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        sysUserService.toggleStatus(id);
        return Result.success(null);
    }

    /**
     * 重置密码
     */
    @PutMapping("/resetPwd/{id}")
    public Result<Void> resetPassword(@PathVariable Long id) {
        sysUserService.resetPassword(id);
        return Result.success(null);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.delete(id);
        return Result.success(null);
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }
}