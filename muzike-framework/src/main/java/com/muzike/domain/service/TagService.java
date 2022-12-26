package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.TagListDto;
import com.muzike.domain.entity.Tag;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-11-08 15:42:19
 */
public interface TagService extends IService<Tag> {

	ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

	ResponseResult addTag(Tag tag);

	ResponseResult deleteTag(Integer id);


	ResponseResult getTag(Integer id);

	ResponseResult updateTag(Tag tag);

	ResponseResult getAllTag();
}

