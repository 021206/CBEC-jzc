package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysRole;
import com.cbec.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cbec.service.SysMenuService;


import java.util.List;
import java.util.Map;


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

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询角色已分配的菜单ID列表
     */
    @GetMapping("/menus/{roleId}")
    public Result<List<Long>> getRoleMenus(@PathVariable Long roleId) {
        return Result.success(sysRoleService.getRoleMenuIds(roleId));
    }

    /**
     * 给角色分配菜单权限
     */
    @PostMapping("/assignMenus")
    public Result<Void> assignMenus(@RequestBody Map<String, Object> params) {
        Long roleId = Long.valueOf(params.get("roleId").toString());
        List<Long> menuIds = ((List<?>) params.get("menuIds")).stream()
                .map(o -> Long.valueOf(o.toString()))
                .collect(java.util.stream.Collectors.toList());
        sysRoleService.assignMenus(roleId, menuIds);
        return Result.success(null);
    }
}