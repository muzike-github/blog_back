package com.muzike.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 更新角色DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {
	private Long id;
	private String roleName;
	private String roleKey;
	private Integer roleSort;
	private String status;
	private List<Long> menuIds;
	private String remark;
}
