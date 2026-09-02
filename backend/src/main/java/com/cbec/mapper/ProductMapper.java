package com.cbec.mapper;

import com.cbec.entity.product.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Insert("INSERT INTO product(sku_code, name, category_id, spec, unit, warning_threshold, status) " +
            "VALUES(#{skuCode}, #{name}, #{categoryId}, #{spec}, #{unit}, #{warningThreshold}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE product SET sku_code=#{skuCode}, name=#{name}, category_id=#{categoryId}, " +
            "spec=#{spec}, unit=#{unit}, warning_threshold=#{warningThreshold}, status=#{status} WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product selectById(@Param("id") Long id);

    @Select("SELECT * FROM product WHERE sku_code = #{skuCode}")
    Product selectBySkuCode(@Param("skuCode") String skuCode);

    // 分页查询（支持关键词搜索）
    @Select("SELECT p.*, c.name as category_name FROM product p " +
            "LEFT JOIN category c ON p.category_id = c.id " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') OR p.sku_code LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY p.id DESC LIMIT #{offset}, #{limit}")
    List<Product> selectPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM product p " +
            "WHERE p.name LIKE CONCAT('%', #{keyword}, '%') OR p.sku_code LIKE CONCAT('%', #{keyword}, '%')")
    int count(@Param("keyword") String keyword);

    // 查询所有启用商品（下拉框用）
    @Select("SELECT * FROM product WHERE status = 1 ORDER BY name ASC")
    List<Product> selectAllEnabled();
}