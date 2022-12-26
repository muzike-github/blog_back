package com.muzike.domain.service;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddArticleDto;
import com.muzike.domain.dto.QueryArticleDto;
import com.muzike.domain.entity.Article;
import com.muzike.domain.vo.PageVo;

import java.util.List;

public interface AddArticleService {
	ResponseResult add(AddArticleDto article);

	PageVo listArticle(QueryArticleDto queryArticleDto);

	Article queryArticleById(Integer id);

	ResponseResult updateArticle(AddArticleDto articleDto);

	ResponseResult deleteArticle(Long id);
}
