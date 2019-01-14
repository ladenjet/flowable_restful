package com.genpact.flowable.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.genpact.flowable.entity.SysRole;

@Mapper
public interface SysRoleDao extends BaseDao<SysRole> {
	List<SysRole> findRoleByUserId(String userId);
}
