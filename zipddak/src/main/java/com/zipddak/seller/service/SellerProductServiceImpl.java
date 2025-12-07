package com.zipddak.seller.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipddak.dto.CategoryDto;
import com.zipddak.dto.ProductDto;
import com.zipddak.entity.Category;
import com.zipddak.entity.Product;
import com.zipddak.entity.ProductOption;
import com.zipddak.repository.CategoryRepository;
import com.zipddak.repository.ProductOptionRepository;
import com.zipddak.repository.ProductRepository;
import com.zipddak.seller.dto.CategoryResponseDto;
import com.zipddak.seller.dto.OptionGroupDto;
import com.zipddak.seller.dto.OptionValueDto;
import com.zipddak.seller.dto.SaveResultDto;
import com.zipddak.seller.dto.SubCategoryResponseDto;
import com.zipddak.seller.repository.SellerProductRepository;
import com.zipddak.util.FileSaveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

	private final CategoryRepository category_repo;
	private final ProductRepository product_repo;
	private final ProductOptionRepository productOpt_repo;
	
	private final SellerProductRepository sellerProduct_repo;
	
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
								            MultipartFile[] detailImageFiles,
								            String optionsJson) throws Exception{
		// 썸네일 파일 저장
		Integer thumbnailIdx = fileSave_svc.uploadFile(thumbnail, productFileUploadPath, "product");

		// 추가이미지 첨부를 한 경우
		// 추가이미지 파일 저장
		Integer[] imageIdxArr = new Integer[5];
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

		//옵션 세팅
		ObjectMapper mapper = new ObjectMapper();
		if (product_dto.getOptionYn() != null && product_dto.getOptionYn()&& optionsJson != null) {
			List<OptionGroupDto> optionGroups = mapper.readValue(optionsJson, new TypeReference<List<OptionGroupDto>>() {});
			
			for (OptionGroupDto optGroup : optionGroups) {
				for (OptionValueDto optValue : optGroup.getValues()) {
					
					ProductOption pdOption = ProductOption.builder()
															.product(productEntity)
															.name(optGroup.getOptionName())  // 색상, 사이즈 등
															.value(optValue.getValue())    // 빨강, 파랑...
															.price(optValue.getPrice())		//옵션 가격 
															.build();
					productOpt_repo.save(pdOption);
				}
			}
		}

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
			msg = "상품 등록 실패.";
		}
	
		return  new SaveResultDto(saveResult, productIdx, msg);
	}

	
	//셀러가 등록한 상품의 카테고리만 조회 
	@Override
	public List<CategoryDto> getSellerCategories(String sellerUsername) throws Exception {
		return sellerProduct_repo.findSellerCategories(sellerUsername);
	}
	
	//특정 셀러의 상품 리스트 
	@Override
	public Map<String, Object> searchMyProductList(String sellerUsername, String visible, String category, String keyword, Integer page) throws Exception {
		PageRequest pr = PageRequest.of(page-1, 10);
		
		//(필터)선택한 판매상태 리스트 
		List<Integer> visibleList = null;
		if (visible != null && !visible.isEmpty()) {
			visibleList = Arrays.stream(visible.split(","))
									.map(Integer::parseInt)
									.collect(Collectors.toList());
		}
		//(필터)선택한 카테고리 리스트 
		List<Integer> categoryList = null;
		if (category != null && !category.isEmpty()) {
		    categoryList = Arrays.stream(category.split(","))
									.map(Integer::parseInt)
									.collect(Collectors.toList());
		}
		
		
		List<ProductDto> myproductsList = sellerProduct_repo.findMyProducts(pr, sellerUsername, visibleList, categoryList, keyword);
		Long totalMyPdCnt = sellerProduct_repo.allMyPdCount(sellerUsername, visibleList, categoryList, keyword);
		
		Integer allPage = (int)(Math.ceil(totalMyPdCnt.doubleValue()/pr.getPageSize()));
		Integer startPage = (page-1)/10*10+1;
		Integer endPage = Math.min(startPage+10-1, allPage);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("curPage", page);
		resultMap.put("allPage", allPage);
		resultMap.put("startPage", startPage);	
		resultMap.put("endPage", endPage);	
		resultMap.put("myproductsList", myproductsList); 
		
		System.out.println("resultMap : " + resultMap);
		
		return resultMap;
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
