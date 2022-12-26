package com.muzike.domain.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新增角色DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDto {
	private String roleName;
	private String roleKey;
	private Integer roleSort;
	private String status;
	private List<Long> menuIds;
	private String remark;
}
