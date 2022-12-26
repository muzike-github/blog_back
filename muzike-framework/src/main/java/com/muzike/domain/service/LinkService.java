package com.muzike.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddLinkDto;
import com.muzike.domain.dto.UpdateLinkDto;
import com.muzike.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-09-05 16:29:14
 */
public interface LinkService extends IService<Link> {

	ResponseResult getAllLink();

	ResponseResult getLinksByParam(Integer pageNum, Integer pageSize, String status, String name);

	ResponseResult addLink(AddLinkDto addLinkDto);

	ResponseResult deleteLink(Long id);

	ResponseResult getLinkById(Long id);

	ResponseResult updateLink(UpdateLinkDto updateLinkDto);
}

