package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddArticleDto;
import com.muzike.domain.dto.QueryArticleDto;
import com.muzike.domain.entity.Article;
import com.muzike.domain.service.AddArticleService;
import com.muzike.domain.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

	@Autowired
	private AddArticleService addArticleService;

	@PostMapping()
	public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
		return addArticleService.add(articleDto);
	}
	//根据条件查询文章列表
	@GetMapping("/list")
	public ResponseResult listArticle(QueryArticleDto queryArticleDto){
		PageVo articles = addArticleService.listArticle(queryArticleDto);
		return ResponseResult.okResult(articles);
	}
	//修改文章,先查询到文章，再修改
	@GetMapping("/{id}")
	public ResponseResult queryArticle(@PathVariable Integer id){
		Article article = addArticleService.queryArticleById(id);
		return ResponseResult.okResult(article);
	}
	//更新文章接口
	@PutMapping()
	public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
		return addArticleService.updateArticle(articleDto);
	}
	//删除文章
	@DeleteMapping("/{id}")
	public ResponseResult deleteArticle(@PathVariable Long id){
		return addArticleService.deleteArticle(id);
	}
}

