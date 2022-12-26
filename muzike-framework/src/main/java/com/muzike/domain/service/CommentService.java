package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-09-21 16:34:49
 */
public interface CommentService extends IService<Comment> {

	ResponseResult getComment(String commentType, Long articleId, Integer pageNum, Integer pageSize);

	ResponseResult addComment(Comment comment);
}

