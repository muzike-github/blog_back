package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping("/getCategoryList")
	public ResponseResult getCategoryList(){
		return categoryService.getCategoryList();
	}
}
