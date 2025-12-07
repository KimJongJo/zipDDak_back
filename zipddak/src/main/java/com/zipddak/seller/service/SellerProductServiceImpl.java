package com.zipddak.seller.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.ProductDto;
import com.zipddak.dto.ProductFileDto;
import com.zipddak.entity.Category;
import com.zipddak.entity.Product;
import com.zipddak.repository.CategoryRepository;
import com.zipddak.repository.ProductRepository;
import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SubCategoryResponseDto;
import com.zipddak.util.FileSaveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

	private final CategoryRepository category_repo;
	private final ProductRepository product_repo;
	private final ModelMapper model_mapper;

	@Autowired
	private FileSaveService fileSave_svc;

	// 첨부파일 저장 경로
	@Value("${productFile.path}")
	private String productFileUploadPath;

	
	// 카테고리 리스트 조회
	@Override
	public List<CategoryResponseDto> getCategoryTree() {

		Category.CategoryType type = Category.CategoryType.product;

		return category_repo.findByDepthAndType(1, type).stream().map(ct -> {
			List<SubCategoryResponseDto> subList = category_repo.findByParentIdxAndType(ct.getCategoryIdx(), type)
					.stream().map(c -> new SubCategoryResponseDto(c.getCategoryIdx(), c.getName()))
					.collect(Collectors.toList());

			return new CategoryResponseDto(ct.getCategoryIdx(), ct.getName(), subList);
		}).collect(Collectors.toList());
	}

	// 상품 등록
	@Override
	@Transactional
	public SaveResultDto productRegist(ProductDto product_dto,
								            MultipartFile thumbnail,
								            MultipartFile[] addImageFiles,
								            MultipartFile[] detailImageFiles) throws Exception{
		// 썸네일 파일 저장
		Integer thumbnailIdx = fileSave_svc.uploadFile(thumbnail, productFileUploadPath, "product");

		// 추가이미지 파일 저장
		Integer[] imageIdxArr = new Integer[5];

		// 추가이미지 첨부를 한 경우
		if (addImageFiles != null) {
			int idx = 0;
			for (MultipartFile f : addImageFiles) {
				if (!f.isEmpty() && idx < 5) {
					imageIdxArr[idx] = fileSave_svc.uploadFile(f, productFileUploadPath, "product");
					idx++;
				}
			}
		}

		// 상세이미지 파일 저장
		Integer[] detailIdxArr = new Integer[2];
		if (detailImageFiles != null) {
			int idx = 0;
			for (MultipartFile f : detailImageFiles) {
				if (!f.isEmpty() && idx < 2) {
					detailIdxArr[idx] = fileSave_svc.uploadFile(f, productFileUploadPath, "product");
					idx++;
				}
			}
		}

		Product productEntity = model_mapper.map(product_dto, Product.class); // dto를 entity로 변환

		// thumbnail 파일 세팅
		productEntity.setThumbnailFileIdx(thumbnailIdx);

		// 추가이미지 image1~image5 자동 매핑
		setFileIdx(productEntity, "Image", imageIdxArr);

		// 상세이미지 detail1~detail2 자동 매핑
		setFileIdx(productEntity, "Detail", detailIdxArr);


		// db저장
		productEntity = product_repo.save(productEntity);
		Boolean saveResult = false;
		Integer productIdx = 0;
		String msg = "";
		
		if(productEntity != null) {
			saveResult = true;
			productIdx = productEntity.getProductIdx();
			msg = "상품 등록이 완료되었습니다.";
			
		}else {
			msg = "상품 등록을 실패했습니다.";
		}
	
		return  new SaveResultDto(saveResult, productIdx, msg);
	}

	
	
	// 엔터티 파일컬럼에 자동 매핑 메소드
	private void setFileIdx(Object entity, String prefix, Integer[] fileIdxArr) {
		try {
			for (int i = 0; i < fileIdxArr.length; i++) {
				if (fileIdxArr[i] == null)
					continue;

				String methodName = "set" + prefix + (i + 1) + "FileIdx";
				Method method = entity.getClass().getMethod(methodName, Integer.class);

				method.invoke(entity, fileIdxArr[i]);
			}

		} catch (Exception e) {
			throw new RuntimeException("파일 인덱스 매핑 실패: " + e.getMessage());
		}
	}

}
