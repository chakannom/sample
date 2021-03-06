package com.chakannom.demo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.chakannom.demo.security.service.LoginService;

@Configuration
public class OAuth2ServerConfig {
	private static final String RESOURCE_ID = "REST_SERVICE";

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId(RESOURCE_ID);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/api/**").hasRole("USER")
				.anyRequest().authenticated();//.and();
				//.requiresChannel().anyRequest().requiresSecure();
			//http.portMapper().http(8080).mapsTo(8443);
		}
	}

	// TokenStore In Memory 방식
	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfigurationInMemory extends AuthorizationServerConfigurerAdapter {
		@Autowired
		@Qualifier("loginService")
		private LoginService loginService;

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.pathMapping("/oauth/token", "/oauth/login") // Path 변경부분
				.userDetailsService(loginService)
				.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
			clients.inMemory()
				.withClient("rest-client")
				.resourceIds(RESOURCE_ID)
				.authorizedGrantTypes("password")
				.authorities("USER")
				.scopes("read", "write", "trust")
				.secret("rest-secret");
			// @formatter:on
		}
	}

	// TokenStore JDBC 방식
	/*
	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfigurationInDatabase extends AuthorizationServerConfigurerAdapter {
		@Autowired
		@Qualifier("loginService")
		private LoginService loginService;

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		private DataSource dataSource;

		@Bean
		public JdbcTokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(tokenStore())
				.userDetailsService(loginService)
				.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
			clients.inMemory()
				.withClient("rest-client")
				.resourceIds(RESOURCE_ID)
				.authorizedGrantTypes("password")
				.authorities("USER")
				.scopes("read", "write", "trust")
				.secret("rest-secret");
			// @formatter:on
		}
	}
	*/

}
