package com.genpact.flowable.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genpact.flowable.entity.Constant;
import com.genpact.flowable.entity.LeaveBill;
import com.genpact.flowable.entity.Result;
import com.genpact.flowable.entity.User;
import com.genpact.flowable.service.LeaveBillService;
@Controller
@RequestMapping("/leavebill")
public class LeaveBillController {
	@Autowired
	private LeaveBillService leaveBillService;

//	@Autowired
//	private ActivitiService activitiService;

	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public Result list(HttpServletRequest request,Principal principal){
		return Result.ok(leaveBillService.findByUserId(Long.parseLong(principal.getName())));
	}



	@RequestMapping(value="/opt",method=RequestMethod.POST)
	@ResponseBody
	public Result opt(@RequestBody LeaveBill leaveBill ,HttpServletRequest request,Principal principal){
		if(leaveBill.getId() == null){
//			leaveBill.setUser(user);
			leaveBill.setUserId(Long.parseLong(principal.getName()));
			leaveBill.setState(0);
			leaveBill.setDeleteFlag(0);
			 leaveBillService.save(leaveBill);
		}else{
			LeaveBill bdObj = leaveBillService.findById(leaveBill.getId());
			User user = bdObj.getUser();
			BeanUtils.copyProperties(leaveBill, bdObj);
			bdObj.setUser(user);
			leaveBillService.save(bdObj);
		}
		return Result.ok();

	}


}
