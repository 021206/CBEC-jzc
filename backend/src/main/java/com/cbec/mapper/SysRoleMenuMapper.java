package com.cbec.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {

    /**
     * 查询某角色拥有的所有菜单ID
     */
    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除某角色的所有菜单关联
     */
    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入角色菜单关联
     */
    @Insert("<script>" +
            "INSERT INTO sys_role_menu(role_id, menu_id) VALUES " +
            "<foreach collection='menuIds' item='menuId' separator=','>" +
            "(#{roleId}, #{menuId})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}