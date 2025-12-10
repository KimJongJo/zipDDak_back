package com.zipddak.admin.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.admin.dto.RequestFormDto;
import com.zipddak.entity.ExpertFile;
import com.zipddak.entity.Request;
import com.zipddak.repository.CategoryRepository;
import com.zipddak.repository.ExpertFileRepository;
import com.zipddak.repository.RequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

	private final RequestRepository requestRepository;
	private final ExpertFileRepository expertFileRepository;
	private final CategoryRepository categoryRepository;

	@Value("${expertFile.path}")
	private String expertFilePath;

	@Override
	public void writeRequest(RequestFormDto requestForm) throws Exception {

		List<Integer> fileIdxList = new ArrayList<Integer>();

		// 1. 이미지 파일 저장하기
		if (requestForm.getFiles() != null) {
			for (MultipartFile file : requestForm.getFiles()) {

				String fileName = file.getOriginalFilename();

				// 확장자
				String ext = fileName.substring(fileName.lastIndexOf("."));

				String fileRename = UUID.randomUUID().toString() + ext;

				ExpertFile expertFile = ExpertFile.builder().fileName(fileName).fileRename(fileRename)
						.storagePath(expertFilePath).build();

				try {
					File saveFile = new File(expertFilePath + File.separator + fileRename);

					// 폴더가 없으면 생성
					if (!saveFile.getParentFile().exists()) {
						saveFile.getParentFile().mkdirs();
					}

					file.transferTo(saveFile); // 실제 파일 저장
				} catch (IOException e) {
					e.printStackTrace();
				}

				ExpertFile savedExpertFile = expertFileRepository.save(expertFile);

				// 저장한 이미지의 아이디를 리스트에 저장
				// -> 요청서 만들때 이미지 아이디를 써야함
				fileIdxList.add(savedExpertFile.getExpertFileIdx());
			}
		}

		// 2. 요청서 생성후 저장

		// 가져온 데이터에서 카테고리 1, 2, 3 에 맞는 idx를 가져와야함
		System.out.println("카테 : " + requestForm.getCate1());
		Integer cate1 = categoryRepository.findByName(requestForm.getCate1()).getCategoryIdx();

		// 시공 견적은 cate2 / 3없음

		int cate2 = 0;
		int cate3 = 0;
		if (cate1 != 74) {
			cate2 = categoryRepository.findByName(requestForm.getCate2()).getCategoryIdx();
			cate3 = categoryRepository.findByName(requestForm.getCate3()).getCategoryIdx();
		}

		Request request = Request.builder().userUsername(requestForm.getUserUsername()).largeServiceIdx(cate1)
				.budget(requestForm.getBudget()).preferredDate(requestForm.getPreferredDate())
				.location(requestForm.getLocation()).constructionSize(requestForm.getConstructionSize())
				.additionalRequest(requestForm.getAdditionalRequest()).purpose(requestForm.getPurpose())
				.place(requestForm.getPlace()).build();

		if (cate1 != 74) {
			request.setMidServiceIdx(cate2);
			request.setSmallServiceIdx(cate3);
		}

		if (fileIdxList.size() > 0) {
			request.setImage1Idx(fileIdxList.get(0));
		}

		if (fileIdxList.size() > 1) {
			request.setImage2Idx(fileIdxList.get(1));
		}

		if (fileIdxList.size() > 2) {
			request.setImage3Idx(fileIdxList.get(2));
		}

		requestRepository.save(request);
	}

}
