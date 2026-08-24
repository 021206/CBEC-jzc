package com.cbec.service;

import com.cbec.entity.Product;
import com.cbec.mapper.ProductMapper;
import com.cbec.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public Product add(Product product) {
        validateProduct(product);
        // 检查SKU是否重复
        Product exist = productMapper.selectBySkuCode(product.getSkuCode());
        if (exist != null) {
            throw new BusinessException("SKU编码已存在");
        }
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        if (product.getWarningThreshold() == null) {
            product.setWarningThreshold(0);
        }
        productMapper.insert(product);
        return product;
    }

    public Product update(Product product) {
        if (product.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        validateProduct(product);
        productMapper.update(product);
        return product;
    }

    public void delete(Long id) {
        productMapper.deleteById(id);
    }

    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    public Map<String, Object> page(String keyword, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        if (keyword == null) keyword = "";

        int offset = (pageNum - 1) * pageSize;
        List<Product> list = productMapper.selectPage(keyword, offset, pageSize);
        int total = productMapper.count(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    public List<Product> getAllEnabled() {
        return productMapper.selectAllEnabled();
    }

    public void toggleStatus(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        Integer currentStatus = product.getStatus();
        if (currentStatus == null) {
            currentStatus = 1;
        }
        product.setStatus(currentStatus == 1 ? 0 : 1);
        productMapper.update(product);
    }

    private void validateProduct(Product product) {
        if (product.getSkuCode() == null || product.getSkuCode().isEmpty()) {
            throw new BusinessException("SKU编码不能为空");
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new BusinessException("商品名称不能为空");
        }
    }
}