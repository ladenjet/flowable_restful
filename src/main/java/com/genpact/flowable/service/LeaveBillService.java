package com.genpact.flowable.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.flowable.dao.LeaveBillDao;
import com.genpact.flowable.entity.LeaveBill;

@Service
public class LeaveBillService {
	@Autowired
	private LeaveBillDao leaveBillDao ;

	public List<LeaveBill> findByUserId(Long id) {
		return leaveBillDao.findByUserIdAndDeleteFlag(id);

	}

	public boolean save(LeaveBill leaveBill) {
		leaveBillDao.save(leaveBill);
		return true;
	}

	public boolean delete(Long id) {
		LeaveBill leaveBill = leaveBillDao.queryObject(id);
		leaveBill.setDeleteFlag(1);
		leaveBillDao.save(leaveBill);
		return true;
	}

	public LeaveBill findById(Long id) {
		return leaveBillDao.queryObject(id);

	}

}
