//package com.muzike.runner;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.muzike.domain.entity.Article;
//import com.muzike.domain.mapper.ArticleMapper;
//import com.muzike.domain.service.ArticleService;
//import com.muzike.utils.RedisCache;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 应用启动后将博客的浏览量存入redis
// */
//@Component
//public class UpdateViewRunner implements CommandLineRunner {
//	@Autowired
//	private ArticleService articleService;
//	@Autowired
//	private RedisCache redisCache;
//	@Override
//	public void run(String... args) throws Exception {
//		List<Article> articles = articleService.list();
//
//		Map<String, Integer> viewCountMap = articles.stream()
//				.collect(Collectors.toMap(article -> article.getId().toString(), article -> {
//					//Integer类型可以在redis中完成自增
//					return article.getViewCount().intValue();
//				}));
//		//存入redis
//		redisCache.setCacheMap("article:viewMap",viewCountMap);
//	}
//}
