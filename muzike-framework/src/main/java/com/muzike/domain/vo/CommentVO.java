package com.muzike.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
	private Long id;

	//文章id
	private Long articleId;
	//根评论id
	private Long rootId;
	//评论内容
	private String content;
	//所回复的目标评论的userid
	private Long toCommentUserId;
	//所回复的目标评论的username
	private String toCommentUserName;
	//回复目标评论id
	private Long toCommentId;

	private Long createBy;

	private Date createTime;

	//评论发表人
	private String username;
	//评论人的头像
	private String avatar;
	//子评论
	private List<CommentVO> children;

}
