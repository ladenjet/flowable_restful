package com.genpact.flowable.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

import com.genpact.flowable.service.UserService;

@Configuration
public class GlobalAuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter  {

	@Autowired
	private UserService userService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

}
