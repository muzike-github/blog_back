package com.muzike.job;

import com.muzike.domain.entity.Article;
import com.muzike.domain.service.ArticleService;
import com.muzike.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时更新浏览量
 */
@Component
public class UpdateViewCountJob {
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private ArticleService articleService;
	//@Scheduled(cron = "0 0/10 * * * ? ")
	public void updateViewCount(){
		//从redis中获取浏览量
		Map<String, Integer> viewMap = redisCache.getCacheMap("article:viewMap");
		//数据处理
		List<Article> articles = viewMap.entrySet()
				.stream()
				.map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
				.collect(Collectors.toList());
		//更新数据库
		articleService.updateBatchById(articles);

	}
}
