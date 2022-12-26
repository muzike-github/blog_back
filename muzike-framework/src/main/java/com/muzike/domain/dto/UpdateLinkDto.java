package com.muzike.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增友链
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLinkDto {
	private Long id;
	private String name;
	private String description;
	private String address;
	private String logo;
	private String status;
}
