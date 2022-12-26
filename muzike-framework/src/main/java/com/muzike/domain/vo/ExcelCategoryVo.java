package com.muzike.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCategoryVo {
	@ExcelProperty("名称")
	private String name;
	//描述
	@ExcelProperty("描述")
	private String description;
	//状态0:正常,1禁用
	private String status;
}
