package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysMenu;
import com.cbec.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/tree")
    public Result<List<SysMenu>> tree() {
        return Result.success(sysMenuService.buildTree());
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.success(sysMenuService.getById(id));
    }

    @PostMapping
    public Result<SysMenu> add(@RequestBody SysMenu menu) {
        return Result.success(sysMenuService.add(menu));
    }

    @PutMapping
    public Result<SysMenu> update(@RequestBody SysMenu menu) {
        if (menu.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(sysMenuService.update(menu));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysMenuService.delete(id);
        return Result.success(null);
    }
}