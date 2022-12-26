package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddCategoryDto;
import com.muzike.domain.dto.UpdateCategoryDto;
import com.muzike.domain.entity.Article;
import com.muzike.domain.entity.Category;
import com.muzike.domain.mapper.CategoryMapper;
import com.muzike.domain.service.ArticleService;
import com.muzike.domain.service.CategoryService;
import com.muzike.domain.vo.CategoryVo;
import com.muzike.domain.vo.PageVo;
import com.muzike.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author muzike
 * @since 2022-08-29 21:55:55
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

	@Autowired
	private ArticleService articleService;
	@Override
	public ResponseResult getCategoryList() {
		//查询文章表，状态为已发布的文章
		LambdaQueryWrapper<Article> articleWrapper=new LambdaQueryWrapper<>();
		articleWrapper.eq(Article::getStatus,0);
		List<Article> articleList=articleService.list(articleWrapper);
		//获取文章的分类id（stream流方式）
		Set<Long> categoryIds = articleList.stream()
				.map(Article::getCategoryId)
				.collect(Collectors.toSet());
		//查询分类表
		List<Category> categories=listByIds(categoryIds);
		categories =categories.stream()
				.filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
				.collect(Collectors.toList());
		//封装vo
		List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
		return ResponseResult.okResult(categoryVos);
	}

	@Override
	public ResponseResult listAllCategory() {
		LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
		wrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
		List<Category> categoryList = list(wrapper);
		List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
		return ResponseResult.okResult(categoryVos);
	}

	//分页查询所有文章分类（可模糊查询）
	@Override
	public ResponseResult listCategoryByParam(Integer pageNum, Integer pageSize, String name, String status) {
		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(Objects.nonNull(name),Category::getName,name);
		wrapper.like(Objects.nonNull(status),Category::getStatus,status);
		Page<Category> page = new Page<>(pageNum, pageSize);
		page(page,wrapper);
		PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(pageVo);
	}
	//新增文章分类
	@Override
	public ResponseResult addCategory(AddCategoryDto categoryDto) {
		Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
		save(category);
		return ResponseResult.okResult();
	}

	//根据id获取分类信息
	@Override
	public ResponseResult getCategoryById(Long id) {
		Category category = getById(id);
		return ResponseResult.okResult(category);
	}

	@Override
	public ResponseResult updateCategory(UpdateCategoryDto categoryDto) {
		Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
		updateById(category);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteCategoryById(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}
}

