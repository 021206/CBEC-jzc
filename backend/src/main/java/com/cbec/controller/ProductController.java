package com.cbec.controller;

import com.cbec.common.Result;
import com.cbec.common.exception.BusinessException;
import com.cbec.entity.product.Product;
import com.cbec.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Result<Product> add(@RequestBody Product product) {
        return Result.success(productService.add(product));
    }

    @PutMapping
    public Result<Product> update(@RequestBody Product product) {
        if (product.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return Result.success(productService.update(product));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @GetMapping("/page")
    public Result<Map<String, Object>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(productService.page(keyword, pageNum, pageSize));
    }

    @GetMapping("/list")
    public Result<List<Product>> listEnabled() {
        return Result.success(productService.getAllEnabled());
    }

    @PutMapping("/status/{id}")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        productService.toggleStatus(id);
        return Result.success(null);
    }
}