package com.zipddak.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zipddak.dto.UserDto;
import com.zipddak.entity.User;
import com.zipddak.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;


	@Override
	public UserDto login(User user) throws Exception {
		userRepository.save(user);
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
