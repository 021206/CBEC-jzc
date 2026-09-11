package com.cbec.service;

import com.cbec.entity.auth.SysRole;
import com.cbec.entity.auth.SysUser;
import com.cbec.mapper.SysMenuMapper;
import com.cbec.mapper.SysRoleMapper;
import com.cbec.mapper.SysRoleMenuMapper;
import com.cbec.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PermissionService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 查询用户的权限标识集合
     * @return null 表示拥有全部权限（超级管理员）
     */
    public Set<String> getUserPermissions(Long userId) {
        // 1. 查询用户
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getRoleId() == null) {
            return Collections.emptySet();
        }

        // 2. 查询角色
        SysRole role = sysRoleMapper.selectById(user.getRoleId());
        if (role == null) {
            return Collections.emptySet();
        }

        // 3. 超级管理员（role_code = admin）拥有全部权限
        if ("admin".equals(role.getRoleCode())) {
            return null;
        }

        // 4. 查询角色关联的菜单ID
        List<Long> menuIds = sysRoleMenuMapper.selectMenuIdsByRoleId(user.getRoleId());
        if (menuIds == null || menuIds.isEmpty()) {
            return Collections.emptySet();
        }

        // 5. 查询菜单中的权限标识
        List<String> perms = sysMenuMapper.selectPermsByMenuIds(menuIds);
        return new HashSet<>(perms);
    }
}