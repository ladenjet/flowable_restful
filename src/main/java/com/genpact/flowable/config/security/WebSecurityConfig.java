package com.genpact.flowable.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.csrf().disable();

		http.formLogin().permitAll()
			.loginPage("/login").permitAll()
			.defaultSuccessUrl("/index")
			.failureUrl("/login?error").permitAll()
			.and()
			.logout().permitAll()
			.and()
			.authorizeRequests()
				// .antMatchers("/" ,"/demo/**" ).permitAll()
//				 .antMatchers("/", "/home").permitAll()
				// .antMatchers("/openapi/**").hasRole("USER")
				.anyRequest().authenticated();
	}

	/**
	 * 忽略需要认证的路径(绕过security)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/bootstrap/**", "/druid/**");
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {

			@Override
		    public String encode(CharSequence charSequence) {
		        return charSequence.toString();
		    }

		    @Override
		    public boolean matches(CharSequence charSequence, String s) {
		        return s.equals(charSequence.toString());
		    }

		});
	}

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth, @Value("${spring.profiles.active}") String env) throws Exception {
//		if ("dev".equals(env)) {
//			auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "ACTUATOR");
//		} else {
//			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//		}
//	}

	// org.springframework.security.crypto.password.PasswordEncoder
	// @Bean
	// public Md5PasswordEncoder passwordEncoder() {
	// return new Md5PasswordEncoder();
	// }
//	@Bean
//	public Md4PasswordEncoder passwordEncoder() {
//		return new Md4PasswordEncoder();
//	}

}