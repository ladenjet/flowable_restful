package com.genpact.flowable.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.genpact.flowable.entity.SysPermission;

@Mapper
public interface SysPermissionDao extends BaseDao<SysPermission> {
	List<SysPermission> findPermissionByUserId(String userId);
}
