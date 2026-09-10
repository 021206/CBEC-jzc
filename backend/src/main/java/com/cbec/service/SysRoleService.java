package com.cbec.service;

import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysRole;
import com.cbec.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cbec.mapper.SysRoleMenuMapper;

import java.util.List;

@Service
public class SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    public List<SysRole> listAll() {
        return sysRoleMapper.selectAll();
    }

    public SysRole getById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    public SysRole add(SysRole role) {
        if (role.getRoleName() == null || role.getRoleName().isEmpty()) {
            throw new BusinessException("角色名称不能为空");
        }
        if (role.getRoleCode() == null || role.getRoleCode().isEmpty()) {
            throw new BusinessException("角色编码不能为空");
        }
        // 检查编码是否重复
        SysRole exist = sysRoleMapper.selectByCode(role.getRoleCode());
        if (exist != null) {
            throw new BusinessException("角色编码已存在");
        }
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        sysRoleMapper.insert(role);
        return role;
    }

    public SysRole update(SysRole role) {
        if (role.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        sysRoleMapper.update(role);
        return role;
    }

    public void delete(Long id) {
        // 检查是否有用户关联该角色
        int count = sysRoleMapper.countUsersByRoleId(id);
        if (count > 0) {
            throw new BusinessException("该角色下还有用户，无法删除");
        }
        sysRoleMapper.deleteById(id);
    }

    public void toggleStatus(Long id) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        role.setStatus(role.getStatus() == 1 ? 0 : 1);
        sysRoleMapper.update(role);
    }

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 查询角色已分配的菜单ID列表
     */
    public List<Long> getRoleMenuIds(Long roleId) {
        return sysRoleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    /**
     * 给角色分配菜单权限
     */
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        // 先删除旧关联
        sysRoleMenuMapper.deleteByRoleId(roleId);
        // 再插入新关联（如果 menuIds 不为空）
        if (menuIds != null && !menuIds.isEmpty()) {
            sysRoleMenuMapper.batchInsert(roleId, menuIds);
        }
    }
}