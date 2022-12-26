package com.muzike.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用于前端添加用户时设置用户权限，展示菜单层级列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuTreeVo {
	private static final long serialVersionUID = 1L;

	/** 节点ID */
	private Long id;

	/** 节点名称 */
	private String label;

	private Long parentId;

	/** 子节点 */
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<MenuTreeVo> children;
}
