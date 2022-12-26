package com.muzike.domain.service.impl;

import com.muzike.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("ps")
public class PermissionService {
	public Boolean hasPermission(String permission){
		if(SecurityUtil.isAdmin()){
			return true;
		}else{
			List<String> permissions = SecurityUtil.getLoginUser().getPermissions();
			if(CollectionUtils.isEmpty(permissions)){
				return false;
			}
			return permissions.contains(permission);
		}
	}
}
