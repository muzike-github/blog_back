package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.Article;
import com.muzike.domain.entity.Category;
import com.muzike.domain.mapper.ArticleMapper;
import com.muzike.domain.service.ArticleService;
import com.muzike.domain.service.CategoryService;
import com.muzike.domain.vo.ArticleDetailVo;
import com.muzike.domain.vo.ArticleListVo;
import com.muzike.domain.vo.HotArticleVo;
import com.muzike.domain.vo.PageVo;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.RedisCache;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RedisCache redisCache;
	@Override
	public ResponseResult hotArticle() {
		//查询热门文章，封装成ResponseResult返回
		LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
		QueryWrapper<Article> wrapper=new QueryWrapper<>();
		//查询条件，必须是正式文章，按照浏览量排序，并且只能查询10条信息
		queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
		queryWrapper.orderByDesc(Article::getViewCount);
		Page<Article> page=new Page<>(1,10);
		page(page,queryWrapper);
		List<Article> articles=page.getRecords();
		articles = articles.stream()
				.map(article -> {
					Integer viewCount = redisCache.getCacheMapValue("article:viewMap", article.getId().toString());
					return article.setViewCount(viewCount.longValue());
				})
				.collect(Collectors.toList());

		//查询完毕后选取我们需要的字段，进行bean的拷贝
//		List<HotArticleVo> articleVos=new ArrayList<>();
//		for (Article article : articles) {
//			HotArticleVo vo=new HotArticleVo();
//			BeanUtils.copyProperties(article,vo);
//			articleVos.add(vo);
//		}
		List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
		return ResponseResult.okResult(articleVos);

	}

	@Override
	public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
		LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
		//查询条件
		//前端如果有categoryId,就和前端一致，
		queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
		// 状态必须是已发布，
		queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
		// 按照isTop值置顶文章优先显示
		queryWrapper.orderByDesc(Article::getIsTop);
		//分页查询
		Page<Article> page=new Page<>(pageNum,pageSize);
		page(page,queryWrapper);

		List<Article> articles = page.getRecords();
		//给categoryName赋值
		//for循环方式
//		for(Article article:articles){
//			Category category = categoryService.getById(article.getCategoryId());
//			article.setCategoryName(category.getName());
//		}
		//steam流方式处理（注意更新文章的浏览量）
		articles= articles.stream()
				.map(article ->
						article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
				.map(article -> {
					Integer viewCount = redisCache.getCacheMapValue("article:viewMap", article.getId().toString());
					return article.setViewCount(viewCount.longValue());
				})
				.collect(Collectors.toList());

		//封装查询结果(转为ArticleListVo)
		// (用steam流操作后，下面的page.getRecords()和上面的articles的值都是一样的)
		List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
		//封装(转为PageVo)
		PageVo pageVo = new PageVo(articleListVos, page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult getArticleDetails(Long id) {
		//查询文章详细信息
		Article article = getById(id);
		//浏览量从redis中直接获取(redis中viewCount为Integer类型，故需要转换为long类型与数据库对应)
		Integer viewCount = redisCache.getCacheMapValue("article:viewMap", id.toString());
		article.setViewCount(viewCount.longValue());//
		//封装为vo
		ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
		//根据分类id查询分类名
		Long categoryId = articleDetailVo.getCategoryId();
		Category category = categoryService.getById(categoryId);
		//
		if(category!=null){
			articleDetailVo.setCategoryName(category.getName());
		}
		return ResponseResult.okResult(articleDetailVo);
	}
	@Override
	public ResponseResult updateViewCount(Long id) {
		//根据id来更新redis中的浏览量
		redisCache.incrementCacheMapValue("article:viewMap",id.toString(),1);
		return ResponseResult.okResult();
	}
}
