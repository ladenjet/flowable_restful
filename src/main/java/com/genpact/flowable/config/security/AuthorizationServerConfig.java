package com.genpact.flowable.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.genpact.flowable.service.UserService;

/**
 * 授权服务器配置类
 * 它主要负责授权服务器的配置
 * 包括：信任的客户端信息的管理、请求令牌的 URL 的配置、令牌的管理、如何认证用户的配置、对于请求令牌的 URL 的安全约束的配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	static final String SIGNING_KEY = "flowable-restful";
	static final String CLIEN_ID = "flowable-client";
	static final String CLIENT_SECRET = "flowable-secret";
	static final String GRANT_TYPE_PASSWORD = "password";
	static final String AUTHORIZATION_CODE = "authorization_code";
    static final String REFRESH_TOKEN = "refresh_token";
    static final String IMPLICIT = "implicit";
	static final String SCOPE_READ = "read";
	static final String SCOPE_WRITE = "write";
    static final String TRUST = "trust";
	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1*60*60;
    static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6*60*60;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
        .inMemory()
		.withClient(CLIEN_ID)
		.secret(CLIENT_SECRET)
		.authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT )
		.scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
		.accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS).
		refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }

    @Bean
	public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
	}


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

}
