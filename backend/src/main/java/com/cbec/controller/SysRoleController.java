package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.annotation.RequiresPermission;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysRole;
import com.cbec.service.SysRoleService;
import com.cbec.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询所有角色
     */
    @RequiresPermission("role:list")
    @GetMapping("/list")
    public Result<List<SysRole>> listAll() {
        return Result.success(sysRoleService.listAll());
    }

    /**
     * 查询角色详情
     */
    @RequiresPermission("role:list")
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    /**
     * 新增角色
     */
    @RequiresPermission("role:add")
    @PostMapping
    public Result<SysRole> add(@RequestBody SysRole role) {
        return Result.success(sysRoleService.add(role));
    }

    /**
     * 编辑角色
     */
    @RequiresPermission("role:edit")
    @PutMapping
    public Result<SysRole> update(@RequestBody SysRole role) {
        if (role.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(sysRoleService.update(role));
    }

    /**
     * 删除角色
     */
    @RequiresPermission("role:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysRoleService.delete(id);
        return Result.success(null);
    }

    /**
     * 切换状态
     */
    @RequiresPermission("role:edit")
    @PutMapping("/status/{id}")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        sysRoleService.toggleStatus(id);
        return Result.success(null);
    }

    /**
     * 查询角色已分配的菜单ID列表
     */
    @RequiresPermission("role:list")
    @GetMapping("/menus/{roleId}")
    public Result<List<Long>> getRoleMenus(@PathVariable Long roleId) {
        return Result.success(sysRoleService.getRoleMenuIds(roleId));
    }

    /**
     * 给角色分配菜单权限
     */
    @RequiresPermission("role:assign")
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