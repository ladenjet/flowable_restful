package com.genpact.flowable.dao;

import org.apache.ibatis.annotations.Mapper;

import com.genpact.flowable.entity.User;
@Mapper
public interface UserDao extends BaseDao<User> {

	User findManagerForEmployee(String employee);

	User findHRForEmployee(String employee);

	User selectUserByUsername(String username);
}
