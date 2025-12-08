package com.zipddak.mypage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.entity.FavoritesProduct;
import com.zipddak.mypage.dto.FavoriteCommunityDto;
import com.zipddak.mypage.dto.FavoriteExpertDto;
import com.zipddak.mypage.dto.FavoriteProductDto;
import com.zipddak.mypage.dto.FavoriteToolDto;
import com.zipddak.mypage.repository.FavoriteDslRepository;
import com.zipddak.repository.FavoritesCommunityRepository;
import com.zipddak.repository.FavoritesExpertRepository;
import com.zipddak.repository.FavoritesProductRepository;
import com.zipddak.repository.FavoritesToolRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

	private final FavoriteDslRepository favoriteDslRepository;
	private final FavoritesProductRepository favoriteProductRepository;
	private final FavoritesToolRepository favoriteToolRepository;
	private final FavoritesExpertRepository favoriteExpertRepository;
	private final FavoritesCommunityRepository favoriteCommunityRepository;

	// 관심 상품목록 조회
	@Override
	public List<FavoriteProductDto> getFavoriteProductList(String username, PageInfo pageInfo) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<FavoriteProductDto> favoriteProductList = favoriteDslRepository.selectFavoriteProductList(username,
				pageRequest);

		// 페이지 수 계산
		Long cnt = favoriteDslRepository.selectFavoriteProductCount(username);

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return favoriteProductList;
	}

	// 상품 좋아요 토글
	@Override
	public Boolean toggleProductLike(String username, Integer productIdx) throws Exception {
		Optional<FavoritesProduct> ofavoritesProduct = favoriteProductRepository
				.findByUserUsernameAndProductIdx(username, productIdx);

		// 좋아요 추가
		if (ofavoritesProduct.isEmpty()) {
			favoriteProductRepository
					.save(FavoritesProduct.builder().userUsername(username).productIdx(productIdx).build());

			return true;
		}
		// 좋아요 삭제
		else {
			favoriteProductRepository.delete(ofavoritesProduct.get());

			return false;
		}
	}

	@Override
	public List<FavoriteToolDto> getFavoriteToolList(String username, PageInfo pageInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FavoriteExpertDto> getFavoriteExpertList(String username, PageInfo pageInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FavoriteCommunityDto> getFavoriteCommunityList(String username, PageInfo pageInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
