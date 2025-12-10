package com.zipddak.mypage.dto;

import java.sql.Date;
import java.util.List;

import com.zipddak.dto.CareerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpertProfileDto {
	private Integer expertIdx; // 전문가 아이디
	private String username; // 전문가 유저아이디

	private String activityName; // 활동명
	private String profileImage; // 프로필 이미지 저장경로

	private String introduction; // 한줄소개
	private String mainService; // 대표 서비스 카테고리

	private String zonecode; // 우편번호
	private String addr1; // 도로명주소
	private String addr2; // 상세주소

	private Integer employeeCount; // 직원 수
	private Date contactStartTime; // 연락가능 시작시간
	private Date contactEndTime; // 연락가능 종료시간

	private String externalLink1; // 외부링크1
	private String externalLink2; // 외부링크2
	private String externalLink3; // 외부링크3

	private List<String> providedService; // 제공 서비스들
	
	private List<CareerDto> careerList; // 경력 리스트
	
	private String providedServiceDesc; // 서비스 상세설명

	private Integer certImage1; // 자격증및기타서류 이미지1 저장경로
	private Integer certImage2; // 자격증및기타서류 이미지2 저장경로
	private Integer certImage3; // 자격증및기타서류 이미지3 저장경로

	private Integer businessLicensePdf; // 사업자등록증 이미지 저장경로

	private List<PortfolioListDto> portfolioList; // 포트폴리오 리스트
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class PortfolioListDto {
		private Integer portfolioIdx; // 포트폴리오 아이디
		private String image1; // 포트폴리오 이미지1 저장경로
	}

	private String questionAnswer1; // 질문1 답변
	private String questionAnswer2; // 질문2 답변
	private String questionAnswer3; // 질문3 답변

	private String settleBank; // 정산 은행명
	private String settleAccount; // 정산 계좌
	private String settleHost; // 정산 예금주
}
