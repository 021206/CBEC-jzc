package com.cbec.service;

import com.cbec.common.exception.BusinessException;
import com.cbec.entity.auth.SysUser;
import com.cbec.mapper.SysUserMapper;
import com.cbec.common.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 分页查询用户
     */
    public Map<String, Object> page(String keyword, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        if (keyword == null) keyword = "";

        int offset = (pageNum - 1) * pageSize;
        List<SysUser> list = sysUserMapper.selectPage(keyword, offset, pageSize);
        int total = sysUserMapper.count(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 新增用户
     */
    public SysUser add(SysUser user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        // 检查用户名是否已存在
        SysUser exist = sysUserMapper.findByUsername(user.getUsername());
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        sysUserMapper.insert(user);
        return user;
    }

    /**
     * 编辑用户
     */
    public SysUser update(SysUser user) {
        if (user.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        SysUser exist = sysUserMapper.selectById(user.getId());
        if (exist == null) {
            throw new BusinessException("用户不存在");
        }
        sysUserMapper.update(user);
        return user;
    }

    /**
     * 切换状态（启用/禁用）
     */
    public void toggleStatus(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        int newStatus = user.getStatus() == 1 ? 0 : 1;
        sysUserMapper.updateStatus(id, newStatus);
    }

    /**
     * 重置密码
     */
    public void resetPassword(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 重置为默认密码 123456
        String encodedPwd = passwordEncoder.encode("123456");
        sysUserMapper.updatePassword(id, encodedPwd);
    }

    /**
     * 删除用户（物理删除）
     */
    public void delete(Long id) {
        sysUserMapper.deleteById(id);
    }

    /**
     * 根据ID查询用户
     */
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }
}