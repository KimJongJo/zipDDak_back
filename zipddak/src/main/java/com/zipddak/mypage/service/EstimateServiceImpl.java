package com.zipddak.mypage.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.zipddak.entity.Estimate;
import com.zipddak.entity.EstimateCost;
import com.zipddak.entity.Expert;
import com.zipddak.mypage.dto.EstimateWriteDto;
import com.zipddak.repository.EstimateCostRepository;
import com.zipddak.repository.EstimateRepository;
import com.zipddak.repository.ExpertRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService {

	private final EstimateRepository estimateRepository;
	private final ExpertRepository expertRepository;
	private final EstimateCostRepository estimateCostRepository;

	// 견적서 보내기
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

	}

}
