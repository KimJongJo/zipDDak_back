package com.zipddak.admin.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CommunityService {

	Integer write(int category, String title, String content, String username, List<MultipartFile> images) throws Exception;

}
