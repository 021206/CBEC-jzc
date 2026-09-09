package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysRole;
import com.cbec.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    public Result<List<SysRole>> listAll() {
        return Result.success(sysRoleService.listAll());
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    @PostMapping
    public Result<SysRole> add(@RequestBody SysRole role) {
        return Result.success(sysRoleService.add(role));
    }

    @PutMapping
    public Result<SysRole> update(@RequestBody SysRole role) {
        if (role.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(sysRoleService.update(role));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysRoleService.delete(id);
        return Result.success(null);
    }

    @PutMapping("/status/{id}")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        sysRoleService.toggleStatus(id);
        return Result.success(null);
    }
}