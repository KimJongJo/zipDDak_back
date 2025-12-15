package com.zipddak.seller.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.zipddak.seller.dto.SearchConditionDto;
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
	public SaveResultDto productRegist(ProductDto product_dto, MultipartFile thumbnail, MultipartFile[] addImageFiles,
			MultipartFile[] detailImageFiles, String optionsJson) throws Exception {
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

		// 옵션 세팅
		ObjectMapper mapper = new ObjectMapper();
		if (product_dto.getOptionYn() != null && product_dto.getOptionYn() && optionsJson != null) {
			List<OptionGroupDto> optionGroups = mapper.readValue(optionsJson,
					new TypeReference<List<OptionGroupDto>>() {
					});

			for (OptionGroupDto optGroup : optionGroups) {
				for (OptionValueDto optValue : optGroup.getValues()) {

					ProductOption pdOption = ProductOption.builder().product(productEntity)
							.name(optGroup.getOptionName()) // 색상, 사이즈 등
							.value(optValue.getValue()) // 빨강, 파랑...
							.price(optValue.getPrice()) // 옵션 가격
							.build();
					productOpt_repo.save(pdOption);
				}
			}
		}

		// db저장
		productEntity = product_repo.save(productEntity);

		Boolean saveResult = false;
		Integer idx = 0;
		String msg = "";
		if (productEntity != null) {
			saveResult = true;
			idx = productEntity.getProductIdx();
			msg = "상품 등록이 완료되었습니다.";

		} else {
			msg = "상품 등록 실패.";
		}

		return new SaveResultDto(saveResult, idx, msg);
	}

	// 셀러가 등록한 상품의 카테고리만 조회
	@Override
	public List<CategoryDto> getSellerCategories(String sellerUsername) throws Exception {
		return sellerProduct_repo.findSellerCategories(sellerUsername);
	}

	// 특정 셀러의 상품 리스트
	public Map<String, Object> searchMyProductList(String sellerUsername,
										            String status,
										            String category,
										            String keyword,
										            Integer page) {
        PageRequest pr = PageRequest.of(page - 1, 10);
        
        // -> status 문자열을 Boolean 리스트로 변환 
        List<Boolean> visibleList = null;
        if (status != null && !status.isEmpty()) {
            visibleList = Arrays.stream(status.split(","))
                    .map(s -> {
                        s = s.trim();
                        if (s.equals("1")) return true;
                        if (s.equals("0")) return false;
                        // fallback: "true"/"false" 같은 문자열을 파싱
                        return Boolean.parseBoolean(s);
                    })
                    .collect(Collectors.toList());
        }

        // 카테고리 (int 리스트)
        List<Integer> categoryList = category != null && !category.isEmpty()
                ? Arrays.stream(category.split(",")).map(Integer::parseInt).collect(Collectors.toList())
                : null;

        SearchConditionDto scDto = SearchConditionDto.builder()
                .sellerUsername(sellerUsername)
                .visibleList(visibleList)
                .categoryList(categoryList)
                .keyword(keyword)
                .build();

        List<ProductDto> myProductList = sellerProduct_repo.searchMyProducts(pr, scDto);
        Long myProductCount = sellerProduct_repo.countMyProducts(scDto);

        int allPage = (int) Math.ceil(myProductCount / 10.0);
        int startPage = (page - 1) / 10 * 10 + 1;
        int endPage = Math.min(startPage + 9, allPage);

        Map<String, Object> result = new HashMap<>();
        result.put("curPage", page);
        result.put("allPage", allPage);
        result.put("startPage", startPage);
        result.put("endPage", endPage);
        result.put("myProductList", myProductList);
        result.put("myProductCount", myProductCount);

        return result;
    }
	
	//상품 디테일 보기 
	@Override
	public ProductDto MyProductDetail(String sellerUsername, Integer productIdx) throws Exception {
		Product productEntity = product_repo.findByProductIdxAndSellerUsername(productIdx, sellerUsername)
			    									.orElseThrow(() -> new IllegalStateException("상품 정보 없음 또는 권한 없음"));
		return productEntity.toProductDetailDto();
		
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
