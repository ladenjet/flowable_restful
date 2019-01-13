package com.genpact.flowable.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author sxia
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        	.anonymous().disable()
            .authorizeRequests()
            //for token
//            .antMatchers("/oauth/token").permitAll()
//                .antMatchers("/users/**").authenticated()
//                .antMatchers(HttpMethod.GET, "/books/**").permitAll()
            //for swagger
            .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
            .anyRequest().authenticated();
    }

}
