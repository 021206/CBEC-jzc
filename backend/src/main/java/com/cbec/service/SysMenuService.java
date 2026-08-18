package com.cbec.service;

import com.cbec.entity.SysMenu;
import com.cbec.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 根据用户 ID 获取菜单树（只返回一级和二级，用于前端渲染侧边栏）
     */
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        // 1. 查询用户所有菜单（平铺列表）
        List<SysMenu> allMenus = sysMenuMapper.selectMenusByUserId(userId);
        if (allMenus == null || allMenus.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 过滤出所有一级菜单（parentId == 0）
        List<SysMenu> rootMenus = allMenus.stream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());

        // 3. 为每个一级菜单组装它的子菜单
        for (SysMenu root : rootMenus) {
            List<SysMenu> children = allMenus.stream()
                    .filter(menu -> menu.getParentId().equals(root.getId()))
                    .collect(Collectors.toList());
            root.setChildren(children);
        }

        return rootMenus;
    }
}