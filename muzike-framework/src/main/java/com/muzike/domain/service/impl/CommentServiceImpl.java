package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.entity.Comment;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.domain.mapper.CommentMapper;
import com.muzike.domain.service.CommentService;
import com.muzike.domain.service.UserService;
import com.muzike.domain.vo.CommentVO;
import com.muzike.domain.vo.PageVo;
import com.muzike.exception.SystemException;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-09-21 16:34:49
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

	@Autowired
	private UserService userService;

	/**
	 *
	 * @param commentType 要查询的评论的类型，文章、友链
	 * @param articleId 如果查询类型为友链，则此处为空，不做eq判断
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public ResponseResult getComment(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
		LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		//根据articleId进行查询
		//如果是查询友链评论，则跳过根据articleId查询
		commentLambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),
				Comment::getArticleId,articleId);
		//评论类型
		commentLambdaQueryWrapper.eq(Comment::getType,commentType);
		//查询根评论(-1)
		commentLambdaQueryWrapper.eq(Comment::getRootId,-1);
		//分页查询
		Page<Comment> page = new Page<>(pageNum, pageSize);
		page(page,commentLambdaQueryWrapper);
		List<CommentVO> commentVOList= toCommentList(page.getRecords());
		//查询根评论的子评论
		for (CommentVO commentVO : commentVOList) {
			commentVO.setChildren(getChildren(commentVO.getId()));
		}

		return ResponseResult.okResult(new PageVo(commentVOList,page.getTotal()));
	}

	/*
	* 增加评论
	* */
	@Override
	public ResponseResult addComment(Comment comment) {
		if(!StringUtils.hasText(comment.getContent())){
			throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
		}
		//根据当前登录人的token获取发表评论人的id
		Long userId = SecurityUtil.getUserId();
		comment.setCreateBy(userId);
		//对某些缺失字段进行填充后保存
		save(comment);
		return ResponseResult.okResult();
	}

	//根据评论的id查询该评论的子评论
	private List<CommentVO> getChildren(Long id){
		LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Comment::getRootId,id);
		lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
		List<Comment> list = list(lambdaQueryWrapper);
		List<CommentVO> commentVOList = toCommentList(list);
		return commentVOList;

	}
	//将查询的comment转换为commentVo并处理某些查询中未处理的字段名()
	private List<CommentVO> toCommentList(List<Comment> commentList){
		List<CommentVO> commentVOList = BeanCopyUtils.copyBeanList(commentList, CommentVO.class);
		for(CommentVO commentVO:commentVOList){
			//通过createBy（评论创建者）查询用户的昵称和头像并赋值
			String nickName = userService.getById(commentVO.getCreateBy()).getNickName();
			String avatar = userService.getById(commentVO.getCreateBy()).getAvatar();
			commentVO.setUsername(nickName);
			commentVO.setAvatar(avatar);
			System.out.println("nike"+nickName);
			System.out.println("avatar"+avatar);
			//如果toCommentID不为-1,则说明是子评论，需要查出该评论回复的是谁的评论
			if(commentVO.getToCommentId()!=-1){
				String toCommentUsername = userService.getById(commentVO.getToCommentUserId()).getNickName();
				commentVO.setToCommentUserName(toCommentUsername);

			}
		}
		return commentVOList;



	}
}

