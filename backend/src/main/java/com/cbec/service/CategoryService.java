package com.cbec.service;

import com.cbec.entity.Category;
import com.cbec.mapper.CategoryMapper;
import com.cbec.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public Category add(Category category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new BusinessException("分类名称不能为空");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        categoryMapper.insert(category);
        return category;
    }

    public Category update(Category category) {
        if (category.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        categoryMapper.update(category);
        return category;
    }

    public void delete(Long id) {
        int childCount = categoryMapper.countChildren(id);
        if (childCount > 0) {
            throw new BusinessException("该分类下存在子分类，无法删除");
        }
        categoryMapper.deleteById(id);
    }

    public Category getById(Long id) {
        return categoryMapper.selectById(id);
    }

    public List<Category> getAll() {
        return categoryMapper.selectAll();
    }

    public List<Category> getAllEnabled() {
        return categoryMapper.selectAllEnabled();
    }

    public List<Category> getTree() {
        List<Category> all = categoryMapper.selectAll();
        if (all == null || all.isEmpty()) {
            return new ArrayList<>();
        }

        List<Category> roots = all.stream()
                .filter(c -> c.getParentId() == 0 || c.getParentId() == null)
                .collect(Collectors.toList());

        for (Category root : roots) {
            buildChildren(root, all);
        }
        return roots;
    }

    private void buildChildren(Category parent, List<Category> all) {
        List<Category> children = all.stream()
                .filter(c -> c.getParentId() != null && c.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());
        parent.setChildren(children);
        for (Category child : children) {
            buildChildren(child, all);
        }
    }
}