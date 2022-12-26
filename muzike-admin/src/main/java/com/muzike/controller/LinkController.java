package com.muzike.controller;

import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddLinkDto;
import com.muzike.domain.dto.UpdateLinkDto;
import com.muzike.domain.service.LinkService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
	@Autowired
	private LinkService linkService;
	//分页查询友链
	@GetMapping("/list")
	public ResponseResult getLinksByParam(Integer pageNum,
	                               Integer pageSize,
	                               String status,
	                               String name){
		return linkService.getLinksByParam(pageNum,pageSize,status,name);
	}
	//新增友链
	@PostMapping("")
	public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
		return linkService.addLink(addLinkDto);
	}
	//删除友链
	@DeleteMapping("/{id}")
	public ResponseResult deleteLink(@PathVariable Long id){
		return linkService.deleteLink(id);
	}
	//修改友链
	@GetMapping("/{id}")
	public ResponseResult getLinkById(@PathVariable Long id){
		return linkService.getLinkById(id);
	}
	@PutMapping("")
	public ResponseResult updateLink(@RequestBody UpdateLinkDto updateLinkDto){
		return linkService.updateLink(updateLinkDto);
	}
}
