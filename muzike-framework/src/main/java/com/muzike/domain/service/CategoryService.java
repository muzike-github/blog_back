package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddCategoryDto;
import com.muzike.domain.dto.UpdateCategoryDto;
import com.muzike.domain.entity.Category;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author muzike
 * @since 2022-08-29 21:55:55
 */
public interface CategoryService extends IService<Category> {

	ResponseResult getCategoryList();

	ResponseResult listAllCategory();

	ResponseResult listCategoryByParam(Integer pageNum, Integer pageSize, String name, String status);

	ResponseResult addCategory(AddCategoryDto categoryDto);

	ResponseResult getCategoryById(Long id);

	ResponseResult updateCategory(UpdateCategoryDto categoryDto);

	ResponseResult deleteCategoryById(Long id);
}

