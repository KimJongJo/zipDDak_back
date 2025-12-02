package com.zipddak.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CorsFilter corsFilter;
	
//	@Autowired
//	private UserRepository userRepository;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{
//		http.addFilter(corsFilter) // 다른 도메인 접근 허용
//		.csrf().disable() // ccsfr 공격 비활성화
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // session 비활성화
//		
//		http.formLogin().disable() // 로그인 폼 비활성화
//		.httpBasic().disable() // httpBasic은 header에 username, password를 암호화하지 않은 상태로 주고받는다. 이를 사용하지 않겠다는 의미
//		.addFilterAt(new JwtAuthenticationFilter(authenticationManager, userRepository), UsernamePasswordAuthenticationFilter.class);
//		
//		http.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
//		.authorizeRequests()
//		.antMatchers("/user/**").authenticated() // 로그인 필요
//		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//		.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER')")
//		.anyRequest().permitAll();
//		
//		return http.build();
//	}
	
	@Bean
	public BCryptPasswordEncoder encoderPassword() {
		return new BCryptPasswordEncoder();
	}
	
}
