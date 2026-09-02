package com.cbec.mapper;

import com.cbec.entity.product.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("INSERT INTO category(parent_id, name, sort_order, status) VALUES(#{parentId}, #{name}, #{sortOrder}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    @Update("UPDATE category SET parent_id=#{parentId}, name=#{name}, sort_order=#{sortOrder}, status=#{status} WHERE id=#{id}")
    int update(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM category WHERE id = #{id}")
    Category selectById(@Param("id") Long id);

    @Select("SELECT * FROM category WHERE status = 1 ORDER BY sort_order ASC")
    List<Category> selectAllEnabled();

    @Select("SELECT * FROM category ORDER BY sort_order ASC")
    List<Category> selectAll();

    @Select("SELECT COUNT(*) FROM category WHERE parent_id = #{parentId}")
    int countChildren(@Param("parentId") Long parentId);
}