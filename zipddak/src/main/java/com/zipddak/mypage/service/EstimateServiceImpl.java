package com.zipddak.mypage.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zipddak.dto.NotificationDto;
import com.zipddak.entity.Estimate;
import com.zipddak.entity.EstimateCost;
import com.zipddak.entity.Expert;
import com.zipddak.entity.Notification.NotificationType;
import com.zipddak.entity.Request;
import com.zipddak.mypage.dto.EstimateWriteDto;
import com.zipddak.mypage.dto.EstimateWriteDto.EstimateCostListDto;
import com.zipddak.mypage.dto.SentEstimateDetailDto;
import com.zipddak.mypage.dto.SentEstimateListDto;
import com.zipddak.mypage.repository.EstimateDslRepository;
import com.zipddak.repository.EstimateCostRepository;
import com.zipddak.repository.EstimateRepository;
import com.zipddak.repository.ExpertRepository;
import com.zipddak.repository.RequestRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService {

	private final EstimateRepository estimateRepository;
	private final ExpertRepository expertRepository;
	private final EstimateCostRepository estimateCostRepository;
	private final EstimateDslRepository estimateDslRepository;
	private final RequestRepository requestRepository;
	private final NotificationServiceImpl notificationService;

	// [전문가]견적서 보내기
	@Override
	public void writeEstimate(EstimateWriteDto estimateWriteDto) throws Exception {
		// 전문가 조회
		Expert expert = expertRepository.findByUser_Username(estimateWriteDto.getUsername()).get();

		// 견적서 insert
		Estimate estimate = Estimate.builder().requestIdx(estimateWriteDto.getRequestIdx())
				.largeServiceIdx(estimateWriteDto.getLargeServiceIdx())
				.diagnosisType(estimateWriteDto.getDiagnosisType()).repairType(estimateWriteDto.getRepairType())
				.demolitionType(estimateWriteDto.getDemolitionType())
				.consultingType(estimateWriteDto.getConsultingType())
				.workDurationType(estimateWriteDto.getWorkDurationType())
				.workDurationValue(estimateWriteDto.getWorkDurationValue()).workScope(estimateWriteDto.getWorkScope())
				.workDetail(estimateWriteDto.getWorkDetail()).disposalCost(estimateWriteDto.getDisposalCost())
				.demolitionCost(estimateWriteDto.getDemolitionCost()).etcFee(estimateWriteDto.getEtcFee())
				.consultingLaborCost(estimateWriteDto.getConsultingLaborCost())
				.stylingDesignCost(estimateWriteDto.getStylingDesignCost())
				.threeDImageCost(estimateWriteDto.getThreeDImageCost())
				.reportProductionCost(estimateWriteDto.getReportProductionCost())
				.costDetail(estimateWriteDto.getCostDetail()).expert(expert).build();

		Estimate saveEstimate = estimateRepository.save(estimate);

		// 견적서 가격 정보 저장
		if (estimateWriteDto.getCostList() != null && !estimateWriteDto.getCostList().isEmpty()) {

			List<EstimateCost> costEntities = estimateWriteDto.getCostList().stream()
					.<EstimateCost>map(costDto -> EstimateCost.builder().estimateIdx(saveEstimate.getEstimateIdx())
							.type(costDto.getType()).label(costDto.getLabel()).amount(costDto.getAmount()).build())
					.collect(Collectors.toList());

			estimateCostRepository.saveAll(costEntities);
		}
		
		// 요청서 조회
		Request request = requestRepository.findById(estimateWriteDto.getRequestIdx()).get();
		
		// 알림 전송
		NotificationDto notificationDto = NotificationDto.builder().type(NotificationType.ESTIMATE)
				.title("새로운 견적 도착").content(expert.getActivityName() + "님이 견적서를 보냈습니다.")
				.recvUsername(request.getUserUsername())
				.sendUsername(estimateWriteDto.getUsername())
				.estimateIdx(saveEstimate.getEstimateIdx()).build();
		
		notificationService.sendNotification(notificationDto);
	}

	// [전문가]보낸 견적서 목록 조회
	@Override
	public List<SentEstimateListDto> getExpertSentEstimateList(String username, PageInfo pageInfo, String status)
			throws Exception {
		// 전문가 조회
		Expert expert = expertRepository.findByUser_Username(username).get();

		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<SentEstimateListDto> estimateList = new ArrayList<>();
		Long cnt = 0L;

		if (status.equals("progress")) {
			estimateList = estimateDslRepository.selectProgressSentEstimateList(expert.getExpertIdx(), pageRequest);
			cnt = estimateDslRepository.selectProgressSentEstimateCount(expert.getExpertIdx());
		} else {
			estimateList = estimateDslRepository.selectFinishSentEstimateList(expert.getExpertIdx(), pageRequest);
			cnt = estimateDslRepository.selectFinishSentEstimateCount(expert.getExpertIdx());
		}

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return estimateList;

	}

	// [전문가]보낸 견적서 상세 조회
	@Override
	public Map<String, Object> getExpertSentEstimateDetail(Integer estimateIdx) throws Exception {
		SentEstimateDetailDto sentEstimateDetail = estimateDslRepository.selectSentEstimateDetail(estimateIdx);
		List<EstimateCostListDto> estimateCostList = estimateDslRepository.selectEstimateCostList(estimateIdx);
		
		Map<String, Object> res = new HashMap<>();
		res.put("sentEstimateDetail", sentEstimateDetail);
		res.put("estimateCostList", estimateCostList);
		
		return res;
	}
}
