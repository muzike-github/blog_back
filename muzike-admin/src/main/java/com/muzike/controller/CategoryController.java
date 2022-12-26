package com.muzike.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddCategoryDto;
import com.muzike.domain.dto.UpdateCategoryDto;
import com.muzike.domain.entity.Category;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.domain.service.CategoryService;
import com.muzike.domain.vo.ExcelCategoryVo;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.WebUtils;
import com.qiniu.util.Json;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	//写博文时查询所有的分类
	@GetMapping("/listAllCategory")
	public ResponseResult listAllCategory(){
		return categoryService.listAllCategory();
	}

	@PreAuthorize("@ps.hasPermission('content:category:export')")
	@GetMapping("/export")
	public void download(HttpServletResponse response) throws IOException {
		try {
			//设置响应头
			WebUtils.setDownloadHeader("测试",response);
			//查询所有分类信息并重新封装vo
			List<Category> categoryList = categoryService.list();
			List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVo.class);
			//写入数据
			EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
					.sheet("模板")
					.doWrite(excelCategoryVos);
		} catch (Exception e) {
			response.reset();
			ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
			WebUtils.renderString(response,JSON.toJSONString(result));
			e.printStackTrace();
		}
	}
	//分类管理时分页查询所有分类()
	@GetMapping("/list")
	public ResponseResult listCategoryByParam(Integer pageNum,
	                                          Integer pageSize,
	                                          String name,
	                                          String status){
		return categoryService.listCategoryByParam(pageNum,pageSize,name,status);
	}
	//新增分类
	@PostMapping("")
	public ResponseResult addCategory(@RequestBody AddCategoryDto categoryDto){
		return categoryService.addCategory(categoryDto);
	}
	//修改分类
	@GetMapping("/{id}")
	public ResponseResult getCategoryById(@PathVariable Long id){
		return categoryService.getCategoryById(id);
	}
	@PutMapping("")
	public ResponseResult updateCategory(@RequestBody UpdateCategoryDto categoryDto){
		return categoryService.updateCategory(categoryDto);
	}
	//删除分类
	@DeleteMapping("/{id}")
	public ResponseResult deleteCategoryById(@PathVariable Long id){
		return categoryService.deleteCategoryById(id);
	}


}
