package com.muzike.utils;

import com.muzike.domain.entity.Article;
import com.muzike.domain.vo.HotArticleVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

//完成对bean的copy,封装为vo
public class BeanCopyUtils {
	private BeanCopyUtils() {
	}

	//单个bean对象的拷贝
	public static <T> T copyBean(Object source,Class<T> clazz){
		T result = null;
		try {
			result=clazz.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(source,result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	//多个对象的拷贝(流式操作)
	public static <o,T> List<T> copyBeanList(List<o> list, Class<T> clazz){
		return list.stream()
				.map(o -> copyBean(o, clazz))
				.collect(Collectors.toList());
	}

//	public static void main(String[] args) {
//		Article article = new Article();
//		article.setId(1L);
//		article.setViewCount(3L);
//		article.setTitle("sfaf");
//		HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
//		System.out.println(hotArticleVo);
//	}
}
