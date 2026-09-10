package com.cbec.service;

import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysMenu;
import com.cbec.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    public List<SysMenu> listAll() {
        return sysMenuMapper.selectAll();
    }

    public SysMenu getById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    public SysMenu add(SysMenu menu) {
        if (menu.getMenuName() == null || menu.getMenuName().isEmpty()) {
            throw new BusinessException("菜单名称不能为空");
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        sysMenuMapper.insert(menu);
        return menu;
    }

    public SysMenu update(SysMenu menu) {
        if (menu.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        sysMenuMapper.update(menu);
        return menu;
    }

    public void delete(Long id) {
        int childCount = sysMenuMapper.countChildren(id);
        if (childCount > 0) {
            throw new BusinessException("该菜单下存在子菜单，无法删除");
        }
        sysMenuMapper.deleteById(id);
    }

    /**
     * 构建菜单树（用于前端展示）
     */
    public List<SysMenu> buildTree() {
        List<SysMenu> all = sysMenuMapper.selectAll();
        // 找出所有顶级菜单（parentId == 0）
        List<SysMenu> roots = all.stream()
                .filter(m -> m.getParentId() == 0 || m.getParentId() == null)
                .collect(Collectors.toList());
        // 为每个顶级菜单递归填充子菜单
        for (SysMenu root : roots) {
            fillChildren(root, all);
        }
        return roots;
    }

    private void fillChildren(SysMenu parent, List<SysMenu> all) {
        List<SysMenu> children = all.stream()
                .filter(m -> m.getParentId() != null && m.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());
        parent.setChildren(children);
        for (SysMenu child : children) {
            fillChildren(child, all);
        }
    }

    /**
     * 根据用户ID获取菜单树（用于动态路由）
     */
    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        List<SysMenu> all = sysMenuMapper.selectMenusByUserId(userId);
        List<SysMenu> roots = all.stream()
                .filter(m -> m.getParentId() == 0 || m.getParentId() == null)
                .collect(Collectors.toList());
        for (SysMenu root : roots) {
            fillChildren(root, all);
        }
        return roots;
    }
}