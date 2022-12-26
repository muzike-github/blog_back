package com.muzike.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.muzike.domain.ResponseResult;
import com.muzike.domain.dto.AddUserDto;
import com.muzike.domain.dto.UpdateUserDto;
import com.muzike.domain.entity.Role;
import com.muzike.domain.entity.User;
import com.muzike.domain.entity.UserRole;
import com.muzike.domain.enums.AppHttpCodeEnum;
import com.muzike.domain.mapper.RoleMapper;
import com.muzike.domain.mapper.UserMapper;
import com.muzike.domain.service.RoleService;
import com.muzike.domain.service.UserRoleService;
import com.muzike.domain.service.UserService;
import com.muzike.domain.vo.PageVo;
import com.muzike.domain.vo.UserInfoAndRoleIdsVo;
import com.muzike.domain.vo.AdminUserInfoVo;
import com.muzike.domain.vo.UserInfoVo;
import com.muzike.exception.SystemException;
import com.muzike.utils.BeanCopyUtils;
import com.muzike.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-16 15:35:17
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleMapper roleMapper;

	/**
	 * 查询前台用户的个人信息
	 * @return
	 */
	@Override
	public ResponseResult getUserInfo() {
		//根据token值查询UserId
		//todo 此处如果token过期，获取不到SecurityContextHolder中的用户信息，则会报错
		Long userId = SecurityUtil.getUserId();
		//根据userId查询用户信息
		User user = getById(userId);
		//封装为UserInfoVo后返回
		UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
		return ResponseResult.okResult(userInfoVo);
	}

	@Override
	public ResponseResult updateUserInfo(User user) {
		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("avatar",user.getAvatar())
				.set("email",user.getEmail())
				.set("nick_name",user.getNickName())
				.set("sex",user.getSex())
				.eq("id",user.getId());
		update(updateWrapper);
		return ResponseResult.okResult();
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public ResponseResult register(User user) {
		//数据非空判断
		if(!StringUtils.hasText(user.getUserName())){
			throw new SystemException(AppHttpCodeEnum.USERNAME_NOTNULL);
		}
		if(!StringUtils.hasText(user.getPassword())){
			throw new SystemException(AppHttpCodeEnum.PASSWORD_NOTNULL );
		}
		if(!StringUtils.hasText(user.getNickName())){
			throw new SystemException(AppHttpCodeEnum.NICKNAME_NOTNULL);
		}
		if(!StringUtils.hasText(user.getEmail())){
			throw new SystemException(AppHttpCodeEnum.EMAIL_NOTNULL);
		}
		//判断用户是否已经存在
		if(userNameExist(user.getUserName())){
			throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
		}
		//判断邮箱是否已经存在
		if(emailExist(user.getEmail())){
			throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
		}
		//对用户密码进行加密
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		save(user);
		return ResponseResult.okResult();
	}


	/**
	 * 后台用户的相关操作
	 * @param userDto
	 * @return
	 */

	//新增角色
	@Override
	@Transactional
	public ResponseResult addUser(AddUserDto userDto) {
		//判断数据非空
		if(Objects.isNull(userDto.getUserName())){
			throw new SystemException(AppHttpCodeEnum.USERNAME_NOTNULL);
		}
		if(Objects.isNull(userDto.getPhonenumber())){
			throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOTNULL);
		}
		if(Objects.isNull(userDto.getEmail())){
			throw new SystemException(AppHttpCodeEnum.EMAIL_NOTNULL);
		}
		//用户名和手机号不能是已存在
		if(userNameExist(userDto.getUserName())){
			throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
		}
		if(emailExist(userDto.getEmail())){
			throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
		}
		String password = passwordEncoder.encode(userDto.getPassword());
		User newUser = BeanCopyUtils.copyBean(userDto, User.class);
		newUser.setPassword(password);
		save(newUser);
		//处理角色对应的权限
		List<UserRole> userRoleList = userDto.getRoleIds().stream()
				.map(id -> new UserRole(newUser.getId(), id))
				.collect(Collectors.toList());
		userRoleService.saveBatch(userRoleList);
		return ResponseResult.okResult();
	}

	//根据条件查询用户
	@Override
	public ResponseResult listUser(Integer pageNum, Integer pageSize,
	                               String userName, String phonenumber,String status) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(Objects.nonNull(userName),User::getUserName,userName);
		wrapper.like(Objects.nonNull(phonenumber),User::getPhonenumber,phonenumber);
		wrapper.like(Objects.nonNull(status),User::getStatus,status);
		Page<User> page = new Page<>(pageNum,pageSize);
		page(page,wrapper);
		PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
		return ResponseResult.okResult(pageVo);
	}

	@Override
	public ResponseResult deleteUser(Long id) {
		removeById(id);
		return ResponseResult.okResult();
	}
	//根据id查询需要修改的用户的信息，用于回显
	@Override
	public ResponseResult selectUserInfo(Long id) {
		//根据角色id查询角色对应的角色id
		List<Long> roleIds = roleMapper.selectRoleIdByUserId(id);
		//查询所有的角色列表
		List<Role> roles = roleService.listAllRole();
		//查询角色的相关信息
		User user = getById(id);
		UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
		return ResponseResult.okResult(vo);
	}
	//后台管理员更新用户的相关信息
	@Override
	@Transactional
	public ResponseResult updateUser(UpdateUserDto userDto) {
		//更新用户信息
		User newUser = BeanCopyUtils.copyBean(userDto, User.class);
		updateById(newUser);
		//更新用户角色信息
		//先删除用户原有的用户-角色对应关系
		LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(UserRole::getUserId,userDto.getId());
		userRoleService.remove(wrapper);
		//再插入新的用户-角色对应关系
		List<UserRole> userRoleList = userDto.getRoleIds().stream()
				.map(id -> new UserRole(userDto.getId(), id))
				.collect(Collectors.toList());
		userRoleService.saveBatch(userRoleList);
		return ResponseResult.okResult();
	}

	/**
	 * 判断用户名是否存在
	 */
	private Boolean userNameExist(String username){
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getUserName,username);
		return (count(queryWrapper)>0);
	}

	/**
	 *
	 * @param email
	 * @return
	 * 判断邮箱是否存在
	 */
	private Boolean emailExist(String email){
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getEmail,email);
		return (count(queryWrapper)>0);
	}
}

