package com.genpact.flowable.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.genpact.flowable.dao.UserDao;
import com.genpact.flowable.entity.User;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	public String findManagerForEmployee(String userId) {
		return userDao.queryObject(userId).getManagerId().toString();
	}

	public String findHRForEmployee(String userId) {
		return userDao.queryObject(userId).getHrId().toString();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.selectUserByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find the user '" + username + "'");
		}
//		String[] roles = new String[] {"ADMIN_ROLE"};
		// Not involve authorities, so pass null to authorities
		String[] roles = new String[] { "ROLE_ADMIN" };
		List<SimpleGrantedAuthority> simpleGrantedAuthorityList = Stream.of(roles).map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

//		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), simpleGrantedAuthorityList);
		return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getPassword(), simpleGrantedAuthorityList);
	}


}
