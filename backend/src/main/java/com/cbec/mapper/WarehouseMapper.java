package com.cbec.mapper;

import com.cbec.entity.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper {

    @Insert("INSERT INTO warehouse(name, address, contact, phone, status) VALUES(#{name}, #{address}, #{contact}, #{phone}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Warehouse warehouse);

    @Update("UPDATE warehouse SET name=#{name}, address=#{address}, contact=#{contact}, phone=#{phone}, status=#{status} WHERE id=#{id}")
    int update(Warehouse warehouse);

    @Delete("DELETE FROM warehouse WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM warehouse WHERE id = #{id}")
    Warehouse selectById(@Param("id") Long id);

    // 分页查询（关键字模糊搜索）
    @Select("SELECT * FROM warehouse WHERE name LIKE CONCAT('%', #{keyword}, '%') ORDER BY id ASC LIMIT #{offset}, #{limit}")
    List<Warehouse> selectPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    // 查询总条数（用于分页）
    @Select("SELECT COUNT(*) FROM warehouse WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    int count(@Param("keyword") String keyword);

    // 查询所有启用的仓库（给下拉框用）
    @Select("SELECT * FROM warehouse WHERE status = 1 ORDER BY id ASC")
    List<Warehouse> selectAllEnabled();
}