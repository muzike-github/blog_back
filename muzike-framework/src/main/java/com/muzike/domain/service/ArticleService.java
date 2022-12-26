package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.Article;
import org.apache.poi.ss.formula.functions.T;

public interface ArticleService extends IService<Article> {
	ResponseResult hotArticle();

	ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

	ResponseResult getArticleDetails(Long id);

	ResponseResult updateViewCount(Long id);
}
