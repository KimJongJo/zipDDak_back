package com.zipddak.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zipddak.dto.UserDto;
import com.zipddak.entity.User;
import com.zipddak.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void join(UserDto userDto) throws Exception {
		//닉네임이 없을시 이름으로 대체
		if(userDto.getNickname() == null || userDto.getNickname().trim().isEmpty()) {
			userDto.setNickname(userDto.getName());
		}
		User user = modelMapper.map(userDto, User.class);
		userRepository.save(user);
	}

	@Override
	public UserDto login(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean checkDoubleId(String username) throws Exception {
		return userRepository.findById(username).isPresent();
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
