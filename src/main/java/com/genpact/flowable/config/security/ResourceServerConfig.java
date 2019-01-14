package com.genpact.flowable.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.genpact.flowable.config.security.interceptor.MyFilterSecurityInterceptor;

/**
 * 配置类
 * 主要负责资源服务器的配置，包括：对于请求资源的 URL 的安全约束的配置等等
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
//    	动态权限
    	http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);

        http.authorizeRequests()
        //authorize to swagger
        .antMatchers("/v2/api-docs",
            			 "/configuration/ui",
            			 "/swagger-resources",
            			 "/configuration/security",
            			 "/swagger-ui.html",
            			 "/webjars/**",
            			 "/swagger-resources/configuration/ui",
            			 "/druid/**")
            .permitAll()

            .anyRequest().authenticated();
    }

}
