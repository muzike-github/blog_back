package com.muzike.domain.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增文章分类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDto {
	private Long id;
	private String name;
	private String status;
	private String description;
}
