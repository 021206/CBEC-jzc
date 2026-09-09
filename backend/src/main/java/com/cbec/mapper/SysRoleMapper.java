package com.cbec.mapper;

import com.cbec.entity.auth.SysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    @Select("SELECT * FROM sys_role ORDER BY id DESC")
    List<SysRole> selectAll();

    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(@Param("id") Long id);

    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode}")
    SysRole selectByCode(@Param("roleCode") String roleCode);

    @Insert("INSERT INTO sys_role(role_name, role_code, status) VALUES(#{roleName}, #{roleCode}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysRole role);

    @Update("UPDATE sys_role SET role_name=#{roleName}, role_code=#{roleCode}, status=#{status} WHERE id=#{id}")
    int update(SysRole role);

    @Delete("DELETE FROM sys_role WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM sys_user WHERE role_id = #{roleId}")
    int countUsersByRoleId(@Param("roleId") Long roleId);
}