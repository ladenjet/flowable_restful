package com.genpact.flowable.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.genpact.flowable.entity.LeaveBill;
@Mapper
public interface LeaveBillDao extends BaseDao<LeaveBill> {
	List<LeaveBill> findByUserIdAndDeleteFlag(Long id);
}
