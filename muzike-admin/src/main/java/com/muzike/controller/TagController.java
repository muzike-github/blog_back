package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.TagListDto;
import com.muzike.domain.entity.Tag;
import com.muzike.domain.service.TagService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

	@Autowired
	private TagService tagService;
	//查询所有标签
	@GetMapping("/list")
	public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
		return tagService.pageTagList(pageNum,pageSize,tagListDto);
	}
	//新增标签
	@PostMapping("")
	public ResponseResult addTag(@RequestBody Tag tag){
		return tagService.addTag(tag);
	}
	//删除标签
	@DeleteMapping("/{id}")
	public ResponseResult deleteTag(@PathVariable("id") Integer id){
		return tagService.deleteTag(id);
	}
	//获取标签内容（用于修改标签）
	@GetMapping("/{id}")
	public ResponseResult getTag(@PathVariable("id") Integer id){
		return tagService.getTag(id);
	}
	//修改标签
	@PutMapping("")
	public ResponseResult updateTag(@RequestBody Tag tag){
		return tagService.updateTag(tag);
	}
	//查询所有标签（写博文时用于展示）
	@GetMapping("/listAllTag")
	public ResponseResult listAllTag(){
		return tagService.getAllTag();
	}
}
