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
import com.zipddak.entity.Category;
import com.zipddak.entity.Expert;
import com.zipddak.entity.ExpertFile;
import com.zipddak.entity.Seller;
import com.zipddak.entity.SellerFile;
import com.zipddak.entity.User;
import com.zipddak.repository.CategoryRepository;
import com.zipddak.repository.ExpertFileRepository;
import com.zipddak.repository.SellerFileRepository;
import com.zipddak.user.dto.ExpertInsertDto;
import com.zipddak.user.dto.SellerInsertDto;
import com.zipddak.user.repository.SignExpertRepository;
import com.zipddak.user.repository.SignSellerRepository;
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
	private SignSellerRepository signSellerRepository;
	
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
	@Autowired
	private SellerFileRepository sellerFileRepository;

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
	public Boolean checkDoubleId(String username) throws Exception {
		return userRepository.findById(username).isPresent();
	}

	@Override
	public Map<Integer, List<CategoryDto>> showExpertCategory(List<Integer> parentIdxList) throws Exception {
		
		Map<Integer, List<CategoryDto>> categoryList = new HashMap<>();
		
		
		for (Integer parentIdx : parentIdxList) {
			List<Category> list = categoryRepository.findByParentIdx(parentIdx);
			for(Category subCategory : list) {
				List<CategoryDto> subList = categoryRepository.findByParentIdx(subCategory.getCategoryIdx())
						.stream()
						.map(c -> modelMapper.map(c, CategoryDto.class))
						.collect(Collectors.toList());
	        
				categoryList.put(subCategory.getCategoryIdx(), subList);
			}
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
	        System.out.println(savedFile);
	        System.out.println(">>>>전문가파일"+expertFileIdx);
	        
	     //2. Expert 등록
	        Expert expert = expertDto.toEntity();
	        expert.setBusinessLicensePdfId(expertFileIdx);

	        signExpertRepository.save(expert);
	    }
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
		
	}

	@Override
	public void joinSeller(SellerInsertDto sellerDto, MultipartFile file, MultipartFile imgFile) throws Exception {
		
		Integer sellerFileIdx = null;
		Integer SellerImgFileIdx = null;

		try {
	    //SellerFile에 파일 저장
	    if (file != null && !file.isEmpty()) {

	        String originName = file.getOriginalFilename();
	        String rename = UUID.randomUUID() + "_" + originName;

	        File saveFile = new File(sellerUpload, rename);
	        file.transferTo(saveFile);

	        SellerFile sellerFile = new SellerFile();
	        sellerFile.setFileName(originName);
	        sellerFile.setFileRename(rename);
	        sellerFile.setStoragePath(sellerUpload);

	        SellerFile savedFile = sellerFileRepository.save(sellerFile);
	        sellerFileIdx = savedFile.getSellerFileIdx(); 
	        System.out.println(savedFile);
	        System.out.println(">>>>셀러파일"+sellerFileIdx);
	    }
	        
	      //SellerFile에 이미지파일 저장
	    if (imgFile != null && !imgFile.isEmpty()) {

	        String originName = imgFile.getOriginalFilename();
	        String rename = UUID.randomUUID() + "_" + originName;

	        File saveImgFile = new File(sellerUpload, rename);
	        imgFile.transferTo(saveImgFile);

	        SellerFile sellerImgFile = new SellerFile();
	        sellerImgFile.setFileName(originName);
	        sellerImgFile.setFileRename(rename);
	        sellerImgFile.setStoragePath(sellerUpload);

	        SellerFile savedImgFile = sellerFileRepository.save(sellerImgFile);
	        SellerImgFileIdx = savedImgFile.getSellerFileIdx(); 
	        System.out.println(savedImgFile);
	        System.out.println(">>>>셀러파일"+SellerImgFileIdx);
	    }
	     
		    //User테이블 등록
	        User user = sellerDto.toUserEntity();
	        user.setNickname(sellerDto.getBrandName());

	        userRepository.save(user);
	        
	        //Seller테이블 등록
	        Seller seller = sellerDto.toSellerEntity(user);
	        seller.setOnlinesalesFileIdx(SellerImgFileIdx);
	        seller.setCompFileIdx(sellerFileIdx);
	        signSellerRepository.save(seller);

	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
		
	}
	
	@Override
	public UserDto login(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	

}
