package com.cbec.mapper;

import com.cbec.entity.auth.SysMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    @Select("SELECT * FROM sys_menu ORDER BY sort_order ASC")
    List<SysMenu> selectAll();

    @Select("SELECT * FROM sys_menu WHERE id = #{id}")
    SysMenu selectById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM sys_menu WHERE parent_id = #{parentId}")
    int countChildren(@Param("parentId") Long parentId);

    @Insert("INSERT INTO sys_menu(parent_id, menu_name, path, component, perms, menu_type, icon, sort_order, status) " +
            "VALUES(#{parentId}, #{menuName}, #{path}, #{component}, #{perms}, #{menuType}, #{icon}, #{sortOrder}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysMenu menu);

    @Update("UPDATE sys_menu SET parent_id=#{parentId}, menu_name=#{menuName}, path=#{path}, component=#{component}, " +
            "perms=#{perms}, menu_type=#{menuType}, icon=#{icon}, sort_order=#{sortOrder}, status=#{status} WHERE id=#{id}")
    int update(SysMenu menu);

    @Delete("DELETE FROM sys_menu WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    // 查询用户拥有的菜单权限（用于动态菜单）
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = 1 " +
            "ORDER BY m.sort_order ASC")
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
}