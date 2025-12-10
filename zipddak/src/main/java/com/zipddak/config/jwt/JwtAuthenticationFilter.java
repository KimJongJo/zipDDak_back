package com.zipddak.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipddak.auth.PrincipalDetails;
import com.zipddak.entity.User;
import com.zipddak.repository.UserRepository;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	@Autowired
	private UserRepository userRepository;
	
	JwtToken jwtToken = new JwtToken();
	
	public JwtAuthenticationFilter (AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
		
	}
	
	// super의 attemptAuthentication 메소드가 실행되고 성공하면 successfulAuthentication가 호출된다.
	// attempteAuthentication 메소드가 리턴해준 Authentication을 파라미터로 받음
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		String username = principalDetails.getUsername();
		
		String accessToken = jwtToken.makeAccessToken(username);
		String refreshToken = jwtToken.makeRefreshToken(username);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("access_token", JwtProperties.TOKEN_PREFIX + accessToken);
		map.put("refresh_token", JwtProperties.TOKEN_PREFIX + refreshToken);
		
		// map에 있는 token을 json 문자열로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String token = objectMapper.writeValueAsString(map);
		System.out.println(token);
		
		response.addHeader(JwtProperties.HEADER_STRING, token);
		response.setContentType("application/json; charset=utf-8");
		
		User user = principalDetails.getUser();
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("username", user.getUsername());
		userInfo.put("name", user.getName());
//		userInfo.put("email", user.getEmail());
//		userInfo.put("address", user.getAddress());
//		userInfo.put("detailAddress", user.getDetailAddress());
//		userInfo.put("roles", user.getRoles());
		
		response.getWriter().write(objectMapper.writeValueAsString(userInfo));

	}
	
}
