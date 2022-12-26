package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
	@Autowired
	private ArticleService articleService;
	//获取文章列表
	@GetMapping("/articleList")
	public ResponseResult articleList(Integer pageNum, Integer pageSize,Long categoryId){

		return articleService.articleList(pageNum,pageSize,categoryId);
	}
	//获取热门文章
	@GetMapping("/hotArticleList")
	public ResponseResult hotArticle(){
		return articleService.hotArticle();
	}
	//获取文章详细信息
	@GetMapping("/{id}")
	public ResponseResult articleDetails(@PathVariable Long id){
		return articleService.getArticleDetails(id);
	}
	//更新文章浏览量
	@PutMapping("/updateViewCount/{id}")
	public ResponseResult updateViewCount(@PathVariable Long id){
		return articleService.updateViewCount(id);
	}




}
