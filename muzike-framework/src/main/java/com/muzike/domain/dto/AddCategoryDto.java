package com.muzike.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增文章分类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDto {
	private String name;
	private String status;
	private String description;
}
