package com.cbec.mapper;

import com.cbec.entity.auth.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysUserMapper {

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser findByUsername(@Param("username") String username);

    @Select("SELECT u.*, r.role_name as roleName " +
            "FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE u.id = #{id}")
    SysUser selectById(@Param("id") Long id);

    @Select("SELECT u.*, r.role_name as roleName " +
            "FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE u.username LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.nickname LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY u.id ASC LIMIT #{offset}, #{limit}")
    List<SysUser> selectPage(@Param("keyword") String keyword,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM sys_user " +
            "WHERE username LIKE CONCAT('%', #{keyword}, '%') " +
            "OR nickname LIKE CONCAT('%', #{keyword}, '%')")
    int count(@Param("keyword") String keyword);

    // 新增用户
    @Insert("INSERT INTO sys_user(username, password, nickname, status, role_id) " +
            "VALUES(#{username}, #{password}, #{nickname}, #{status}, #{roleId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser user);

    // 编辑用户
    @Update("UPDATE sys_user SET username=#{username}, nickname=#{nickname}, status=#{status}, role_id=#{roleId} WHERE id=#{id}")
    int update(SysUser user);

    @Update("UPDATE sys_user SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE sys_user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}