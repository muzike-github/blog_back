package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddArticleDto;
import com.muzike.domain.dto.QueryArticleDto;
import com.muzike.domain.entity.Article;
import com.muzike.domain.entity.ArticleTag;
import com.muzike.domain.mapper.ArticleMapper;
import com.muzike.domain.service.AddArticleService;
import com.muzike.domain.service.ArticleTagService;
import com.muzike.domain.vo.PageVo;
import com.muzike.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AddArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements AddArticleService {
	@Autowired
	private ArticleTagService articleTagService;

	@Override
	@Transactional
	public ResponseResult add(AddArticleDto articleDto) {
		Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
		save(article);
		//流式操作将文章的每个标签和文章对应起来
		List<ArticleTag> articleTags = articleDto.getTags().stream()
				.map(tagId -> new ArticleTag(article.getId(), tagId))
				.collect(Collectors.toList());
		//添加文章的标签
		articleTagService.saveBatch(articleTags);
		return ResponseResult.okResult();
	}

	@Override
	public PageVo listArticle(QueryArticleDto queryArticleDto) {
		LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
		//根据文章标题和摘要进行模糊查询
		wrapper.like(Objects.nonNull(queryArticleDto.getTitle()),
				Article::getTitle,queryArticleDto.getTitle());
		wrapper.like(Objects.nonNull(queryArticleDto.getSummary()),
				Article::getSummary,queryArticleDto.getSummary());
		wrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL);
		wrapper.orderByDesc(Article::getIsTop);
		Page<Article> page = new Page<>(queryArticleDto.getPageNum(),queryArticleDto.getPageSize());
		page(page,wrapper);
		List<Article> articleList = page.getRecords();
		PageVo pageVo = new PageVo(articleList, page.getTotal());
		return pageVo;
	}

	@Override
	public Article queryArticleById(Integer id) {
		Article article = getById(id);
		return article;
	}

	@Override
	public ResponseResult updateArticle(AddArticleDto articleDto) {
		Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
		updateById(article);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteArticle(Long id) {
		removeById(id);//此处配置了mybatisPlus，故此处的删除为逻辑删除
		return ResponseResult.okResult();
	}
}
