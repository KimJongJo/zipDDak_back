package com.zipddak.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.ToolDto;
import com.zipddak.entity.Tool.ToolStatus;
import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.dto.ToolCardsMoreDto;
import com.zipddak.user.dto.ToolDetailviewDto;

public interface ToolService {
	
	//공구 리스트
	ToolCardsMoreDto toolCardsToolMain (String categoryNo, String keyword, String username,
			Integer wayNo, Integer orderNo, Boolean rentalState,Integer offset, Integer size)throws Exception;
	
	//공구 등록
	Integer ToolRegist(ToolDto toolDto, MultipartFile thumbnail,List<MultipartFile> imgs)throws Exception;
	
	//공구 선택
	ToolDto toolSelect (Integer toolIdx) throws Exception;
	
	//공구 수정
	void ToolModify(ToolDto toolDto, MultipartFile thumbnail,List<MultipartFile> imgs, List<Integer>imageIndexes)throws Exception;
	
	//내공구
	ToolCardsMoreDto myTools (String username, Integer toolStatusNo, Integer size, Integer offset)throws Exception;
	
	//공구 삭제
	ToolStatus delteTool (String username, Integer toolIdx)throws Exception;
	
	//공구 상태 변경
	ToolStatus stopTool (String username, Integer toolIdx)throws Exception;
	
	//공구 상세
	ToolDetailviewDto targetTool (Integer toolIdx, String username) throws Exception;
	
	//유저의 다른 공구
	ToolCardsDto ownersTool (String username, String owner, Integer toolIdx)throws Exception;
	
	//공구 리뷰
	Map<String,Object> toolsReview (Integer toolIdx, Integer page, Integer orderNo)throws Exception;

}
