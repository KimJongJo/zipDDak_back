package com.zipddak.user.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.UserDto;
import com.zipddak.entity.Category.CategoryType;
import com.zipddak.entity.Expert;
import com.zipddak.entity.ExpertFile;
import com.zipddak.entity.User;
import com.zipddak.repository.CategoryRepository;
import com.zipddak.repository.ExpertFileRepository;
import com.zipddak.user.dto.ExpertInsertDto;
import com.zipddak.user.repository.SignExpertRepository;
import com.zipddak.user.repository.UserRepository;

@Service
public class SignUpServiceImpl implements SignUpService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SignExpertRepository signExpertRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${file.upload.expert}")
	private String expertUpload;
	@Autowired
	private ExpertFileRepository expertFileRepository;
	
	@Value("${file.upload.profile}")
	private String profileUpload;
	@Value("${file.upload.seller}")
	private String sellerUpload;

	@Override
	public void joinUser(UserDto userDto) throws Exception {
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
	public Map<Integer, List<CategoryDto>> showExpertCategory(List<Integer> parentIdxList, CategoryType  type) throws Exception {
		
		Map<Integer, List<CategoryDto>> categoryList = new HashMap<>();
		
		for (Integer parentIdx : parentIdxList) {

	        List<CategoryDto> list =
	            categoryRepository.findByParentIdxAndType(parentIdx, type)
	            .stream()
	            .map(c -> modelMapper.map(c, CategoryDto.class))
	            .collect(Collectors.toList());
	        
	        categoryList.put(parentIdx, list);
	        
		}
		
		return categoryList;
	}

	@Override
	@Transactional
	public void joinExpert(ExpertInsertDto expertDto, MultipartFile file) throws Exception {
		
		Integer expertFileIdx = null;

		try {
	    //1. 파일이 있을 경우 ExpertFile에 먼저 저장
	    if (file != null && !file.isEmpty()) {

	        String originName = file.getOriginalFilename();
	        String rename = UUID.randomUUID() + "_" + originName;

	        File saveFile = new File(expertUpload, rename);
	        file.transferTo(saveFile);

	        ExpertFile expertFile = new ExpertFile();
	        expertFile.setFileName(originName);
	        expertFile.setFileRename(rename);
	        expertFile.setStoragePath(expertUpload);

	        ExpertFile savedFile = expertFileRepository.save(expertFile);
	        expertFileIdx = savedFile.getExpertFileIdx(); 
	        
	        System.out.println(">>>>파일"+expertFileIdx);
	        
	     //2. Expert 저장
	        Expert expert = modelMapper.map(expertDto, Expert.class);
	        expert.setProvidedServiceIdx(expertDto.getProvidedServiceIdx());
	        expert.setBusinessLicensePdfId(expertFileIdx);
	        
	        User user = userRepository.findById(expertDto.getUserUsername())
	        	    .orElseThrow(() -> new RuntimeException("User not found"));
	        expert.setUser(user);

	        signExpertRepository.save(expert);
	    }
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
		
	}


	

}
