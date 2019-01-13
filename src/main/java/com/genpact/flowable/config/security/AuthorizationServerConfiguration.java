package com.genpact.flowable.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @author sxia
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients
//            .inMemory()
//                .withClient("client")
//                    .authorizedGrantTypes("client-credentials","password", "refresh_token")
//                    .scopes("read", "write")
//                    .accessTokenValiditySeconds(5000)
//                    .secret("secret").refreshTokenValiditySeconds(50000);
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
//        .tokenStore(tokenStore)
//        .authenticationManager(authenticationManager)
//        .userDetailsService(userService);
//    }
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//    	  security
//          .tokenKeyAccess("permitAll()")
//          .checkTokenAccess("isAuthenticated()")
//          .allowFormAuthenticationForClients();
//    }
//
//    @Bean
//    @Primary
//    public DefaultTokenServices tokenServices() {
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setSupportRefreshToken(true); // support refresh token
//        tokenServices.setTokenStore(tokenStore); // use in-memory token store
//        return tokenServices;
//    }


	 	@Autowired
	    private AuthenticationManager authenticationManager;

	    @Override
	    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	        security
//	                .tokenKeyAccess("permitAll()")
//	                .checkTokenAccess("isAuthenticated()")
	                .allowFormAuthenticationForClients();
	    }

	    @Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	        clients.inMemory().withClient("flowable-client")
	                .authorizedGrantTypes("client-credentials", "password","refresh_token")
	                .authorities("ROLE_CLIENT", "ROLE_ANDROID_CLIENT")
	                .scopes("read", "write", "trust")
//	                .resourceIds("oauth2-resource")
	                .accessTokenValiditySeconds(5000)
	                .secret("flowable-secret").refreshTokenValiditySeconds(50000);
	    }

	    @Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        endpoints.authenticationManager(authenticationManager)
	                 .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
	                 .tokenEnhancer(new CustomTokenEnhancer());
	    }

}
