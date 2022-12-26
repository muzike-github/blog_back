package com.muzike.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于修改角色时信息回显
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
	private Long id;
	private String remark;
	private String roleKey;
	private String roleName;
	private String roleSort;
	private String status;
}
