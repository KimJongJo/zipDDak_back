package com.zipddak.config.oauth;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;
	
	public GoogleUserInfo(Map<String, Object> attributes, Map<String, Object> properties) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("aud"));
	}

	@Override
	public String getProvider() {
		return "Google";
	}

	@Override
	public String getEmail() {
		return null;
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getProfileImage() {
		return (String)attributes.get("picture"); 
	}


}
