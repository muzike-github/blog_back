package com.muzike.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryArticleDto {
	private Integer pageNum;
	private Integer pageSize;
	//根据文章标题查询
	private String title;
	//根据文章摘要查询
	private String summary;
}
