package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.constants.SystemConstants;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddLinkDto;
import com.muzike.domain.dto.UpdateLinkDto;
import com.muzike.domain.entity.Link;
import com.muzike.domain.mapper.LinkMapper;
import com.muzike.domain.service.LinkService;
import com.muzike.domain.vo.PageVo;
import com.muzike.domain.vo.linkVo;
import com.muzike.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-09-05 16:29:14
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

	@Override
	public ResponseResult getAllLink() {
		//查询所有审核通过的友链
		LambdaQueryWrapper<Link> lambdaQueryWrapper=new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
		List<Link> links=list(lambdaQueryWrapper);
		//转换为VO
		List<linkVo> linkVos = BeanCopyUtils.copyBeanList(links, linkVo.class);
		return ResponseResult.okResult(linkVos);
	}

	@Override
	public ResponseResult getLinksByParam(Integer pageNum, Integer pageSize, String status, String name) {
		LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(Objects.nonNull(status),Link::getStatus,status);
		wrapper.like(Objects.nonNull(name),Link::getName,name);
		Page<Link> linkPage = new Page<>(pageNum, pageSize);
		page(linkPage,wrapper);
		PageVo pageVo = new PageVo(linkPage.getRecords(), linkPage.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	//增加友链
	@Override
	public ResponseResult addLink(AddLinkDto addLinkDto) {
		Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
		save(link);
		return ResponseResult.okResult();
	}

	@Override
	public ResponseResult deleteLink(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}
	//根据id查询友链信息回显
	@Override
	public ResponseResult getLinkById(Long id) {
		Link link = getById(id);
		return ResponseResult.okResult(link);
	}
	//更新友链
	@Override
	public ResponseResult updateLink(UpdateLinkDto updateLinkDto) {
		Link link = BeanCopyUtils.copyBean(updateLinkDto, Link.class);
		updateById(link);
		return ResponseResult.okResult();
	}
}

