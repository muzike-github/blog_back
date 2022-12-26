package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.TagListDto;
import com.muzike.domain.entity.LoginUser;
import com.muzike.domain.entity.Tag;
import com.muzike.domain.mapper.TagMapper;
import com.muzike.domain.service.TagService;
import com.muzike.domain.vo.PageVo;
import com.muzike.domain.vo.TagVo;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 15:42:19
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

	@Override
	public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
		//分页查询
		LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
		wrapper.like(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
		Page<Tag> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page(page,wrapper);
		//封装数据返回
		PageVo pageVo=new PageVo(page.getRecords(),page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult addTag(Tag tag) {
		//mybatis-plus会自动更新创建时间，创建人，更新时间，更新人
		save(tag);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteTag(Integer id) {
		removeById(id);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult getTag(Integer id) {
		LambdaQueryWrapper<Tag> wrapper=new LambdaQueryWrapper<>();
		wrapper.eq(Tag::getId,id);
		Tag tag = getById(id);
		TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
		return ResponseResult.okResult(tagVo);
	}

	@Override
	public ResponseResult updateTag(Tag tag) {
		updateById(tag);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult getAllTag() {
		List<Tag> tags = list();
		List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags, TagVo.class);
		return ResponseResult.okResult(tagVos);
	}

}

