package com.zipddak.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zipddak.dto.UserDto;
import com.zipddak.entity.User;
import com.zipddak.repository.UserRepository;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto login(String username, String password) throws Exception {
		
		User user = userRepository.findById(username).orElseThrow(()-> new Exception("username오류"));		
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public Boolean checkExpert(String username) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean checkAddr(String username) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
