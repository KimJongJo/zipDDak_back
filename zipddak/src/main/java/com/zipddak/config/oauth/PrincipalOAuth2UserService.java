package com.zipddak.config.oauth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{

//	@Autowired
//	private UserRepository userRepository;
//	
//	@Override
//	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		OAuth2User oAuth2User = super.loadUser(userRequest);
//		return processOAuth2User(userRequest, oAuth2User);
//	}
//
//	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
//		String registrationId = userRequest.getClientRegistration().getRegistrationId();
//		System.out.println(registrationId);
//		
//		OAuth2UserInfo oAuth2UserInfo = null;
//		
//		if(registrationId.equals("naver")) {
//			System.out.println("naver");
//			System.out.println(oAuth2User.getAttributes());
//			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttribute("response"));
//		}else if(registrationId.equals("kakao")) {
//			System.out.println("kakao");
//			System.out.println(oAuth2User.getAttributes());
//			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes(), oAuth2User.getAttribute("properties"));
//		}else {
//			System.out.println("네이버와 카카오만 지원");
//			return null;
//		}
//		
//		// 1. DB 에서 조회
//		Optional<User> ouser = userRepository.findByProviderIdAndProvider(oAuth2UserInfo.getProviderId(), oAuth2UserInfo.getProvider());
//		User user = null;
//		if(ouser.isEmpty()) { // 가입
//			user = User.builder()
//					.username(oAuth2UserInfo.getProviderId())
//					.email(oAuth2UserInfo.getEmail())
//					.provider(oAuth2UserInfo.getProvider())
//					.providerId(oAuth2UserInfo.getProviderId())
//					.profileImage(oAuth2UserInfo.getProfileImage())
//					.name(oAuth2UserInfo.getName())
//					.roles("ROLE_USER")
//					.build();
//			
//		} else { // 정보 변경
//			user = ouser.get();
//			user.setEmail(oAuth2UserInfo.getEmail());
//			user.setName(oAuth2UserInfo.getName());
//			user.setProfileImage(oAuth2UserInfo.getProfileImage());
//		}
//		
//		userRepository.save(user);
//		
//		return new PrincipalDetails(user, oAuth2User.getAttributes());
//	}
	
}
