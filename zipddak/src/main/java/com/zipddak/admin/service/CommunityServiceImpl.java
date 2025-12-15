package com.zipddak.admin.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.entity.Community;
import com.zipddak.entity.CommunityFile;
import com.zipddak.entity.User;
import com.zipddak.repository.CommunityFileRepository;
import com.zipddak.repository.CommunityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
	
	@Value("${communityFile.path}")
	private String filePath;
	
	private final CommunityRepository communityRepository;
	private final CommunityFileRepository communitFileRepository;
	
	@Override
	public Integer write(int category, String title, String content, String username, List<MultipartFile> images)
			throws Exception {

		
		System.out.println("filePath : " + filePath);
		
		List<Integer> savedFileIdxs = new ArrayList<>();
		
		if (images != null && !images.isEmpty()) {
			
			for(MultipartFile file : images) {
				
				String originalFilename = file.getOriginalFilename();
				String extension = "";
	            if (originalFilename != null && originalFilename.contains(".")) {
	                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
	            }

	            String storedFileName = UUID.randomUUID().toString() + extension;
	            File dest = new File(filePath + File.separator + storedFileName);

	            // 폴더 없으면 생성
	            if (!dest.getParentFile().exists()) {
	                dest.getParentFile().mkdirs(); // 상위 폴더가 없으면 생성
	            }
	            file.transferTo(dest);
	            
	            
	            CommunityFile communityfile = CommunityFile.builder()
	            								.fileName(originalFilename)
	            								.fileRename(storedFileName)
	            								.storagePath(filePath)
	            								.build();
	            
	            CommunityFile saveFile = communitFileRepository.save(communityfile);
	            
	            savedFileIdxs.add(saveFile.getCommunityFileIdx());
				
			}
			
		}		
		
		
		Community community = Community.builder()
								.category(category)
								.title(title)
								.content(content)
								.user(User.builder()
										.username(username)
										.build())
								.img1(savedFileIdxs.size() > 0 ? savedFileIdxs.get(0) : null)
					            .img2(savedFileIdxs.size() > 1 ? savedFileIdxs.get(1) : null)
					            .img3(savedFileIdxs.size() > 2 ? savedFileIdxs.get(2) : null)
					            .img4(savedFileIdxs.size() > 3 ? savedFileIdxs.get(3) : null)
					            .img5(savedFileIdxs.size() > 4 ? savedFileIdxs.get(4) : null)
								.build();
										
		Community saveCommunity = communityRepository.save(community);
								
		return saveCommunity.getCommunityIdx();
		
	}

}
