package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.annotation.RequiresPermission;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.product.Category;
import com.cbec.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequiresPermission("category:add")
    @PostMapping
    public Result<Category> add(@RequestBody Category category) {
        return Result.success(categoryService.add(category));
    }

    @RequiresPermission("category:edit")
    @PutMapping
    public Result<Category> update(@RequestBody Category category) {
        if (category.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(categoryService.update(category));
    }

    @RequiresPermission("category:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success(null);
    }

    @RequiresPermission("category:list")
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @RequiresPermission("category:list")
    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.getAll());
    }

    @RequiresPermission("category:list")
    @GetMapping("/tree")
    public Result<List<Category>> tree() {
        return Result.success(categoryService.getTree());
    }
}