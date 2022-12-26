package com.muzike.controller;

import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.Comment;
import com.muzike.domain.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论")
public class CommentController {

	@Autowired
	private CommentService commentService;
	@GetMapping("/commentList")
	@ApiOperation(value = "评论列表",notes = "获取所有评论")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "articleId",value = "文章id"),
			@ApiImplicitParam(name = "pageNum",value = "页数"),
			@ApiImplicitParam(name = "pageSize",value = "每页显示评论数量")
	})
	public ResponseResult getComment(Long articleId, Integer pageNum, Integer pageSize){
		return commentService.getComment(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
	}

	@PostMapping()
	public ResponseResult addComment(@RequestBody Comment comment){
		return commentService.addComment(comment);
	}
	//查询友链评论时，传入的articleId为null
	@GetMapping("/linkCommentList")
	public ResponseResult getLinkComment(Long articleId,Integer pageNum,Integer pageSize){
		return commentService.getComment(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
	}
}
