package com.zipddak.user.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.dto.ToolDto;
import com.zipddak.entity.Tool;
import com.zipddak.entity.Tool.ToolStatus;
import com.zipddak.entity.ToolFile;
import com.zipddak.repository.ToolFileRepository;
import com.zipddak.repository.ToolRepository;
import com.zipddak.user.dto.ToolCardsDto;
import com.zipddak.user.dto.ToolCardsMoreDto;
import com.zipddak.user.dto.ToolDetailviewDto;
import com.zipddak.user.dto.ToolReviewDto;
import com.zipddak.user.repository.ToolCardDsl;

@Service
public class ToolServiceImpl implements ToolService {

	@Autowired
	private ToolCardDsl toolCardDsl;

	@Autowired
	private ToolRepository toolRepository;

	@Autowired
	private ToolFileRepository toolFileRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${toolFile.path}")
	private String toolfileUpload;

	// 공구 메인
	@Override
	public ToolCardsMoreDto toolCardsToolMain(String categoryNo, String keyword, String username, Integer wayNo,
			Integer orderNo, Boolean rentalState, Integer offset, Integer size) throws Exception {
		ToolCardsDto toolCards = toolCardDsl.toolsToolMain(categoryNo, keyword, username, wayNo, orderNo, rentalState,
				offset, size);

		boolean hasNext = (offset + 1) * size < toolCards.getTotalCount();

		return new ToolCardsMoreDto(toolCards.getCards(), toolCards.getTotalCount(), hasNext);
	}

	// 공구 사진 저장
	public Integer saveToolFile(MultipartFile file) throws Exception {

		String originName = file.getOriginalFilename();
		String rename = UUID.randomUUID() + "_" + originName;

		File saveFile = new File(toolfileUpload, rename);
		file.transferTo(saveFile);

		ToolFile toolFile = new ToolFile();
		toolFile.setFileName(originName);
		toolFile.setFileRename(rename);
		toolFile.setStoragePath(toolfileUpload);

		ToolFile saved = toolFileRepository.save(toolFile);
		System.out.println("1saved : " + saved);
		return saved.getToolFileIdx();
	}

	// 공구 등록
	@Override
	@Transactional
	public Integer ToolRegist(ToolDto toolDto, MultipartFile thumbnail, List<MultipartFile> imgs) throws Exception {

		// 썸네일 저장
		Integer thumbnailIdx = null;
		if (thumbnail != null && !thumbnail.isEmpty()) {
			thumbnailIdx = saveToolFile(thumbnail);
		}
		System.out.println("11 : " + thumbnailIdx);
		// 상세 이미지 저장
		Integer[] imgIdx = new Integer[5];

		if (imgs != null) {
			for (int i = 0; i < imgs.size() && i < 5; i++) {
				MultipartFile img = imgs.get(i);
				if (img == null || img.isEmpty())
					continue;

				imgIdx[i] = saveToolFile(img);
			}
		}

		// Tool 생성
		Tool tool = modelMapper.map(toolDto, Tool.class);

		tool.setThunbnail(thumbnailIdx);
		tool.setImg1(imgIdx[0]);
		tool.setImg2(imgIdx[1]);
		tool.setImg3(imgIdx[2]);
		tool.setImg4(imgIdx[3]);
		tool.setImg5(imgIdx[4]);

		Tool savedTool = toolRepository.save(tool);
		toolRepository.flush();

		return savedTool.getToolIdx();

	}

	// 기존 이미지 가져오기
	private Integer getOldImgIdx(Tool tool, int index) {
		switch (index) {
		case 0:
			return tool.getImg1();
		case 1:
			return tool.getImg2();
		case 2:
			return tool.getImg3();
		case 3:
			return tool.getImg4();
		case 4:
			return tool.getImg5();
		default:
			return null;
		}
	}

	// 새 이미지 채우기
	private void setToolImgIdx(Tool tool, int index, Integer idx) {
		switch (index) {
		case 0:
			tool.setImg1(idx);
			break;
		case 1:
			tool.setImg2(idx);
			break;
		case 2:
			tool.setImg3(idx);
			break;
		case 3:
			tool.setImg4(idx);
			break;
		case 4:
			tool.setImg5(idx);
			break;
		}
	}

	// 공구 수정
	@Override
	public void ToolModify(ToolDto toolDto, MultipartFile thumbnail, List<MultipartFile> imgs) throws Exception {

		Tool oldTool = toolRepository.findById(toolDto.getToolIdx())
				.orElseThrow(() -> new RuntimeException("Tool not found"));
		modelMapper.map(toolDto, oldTool); // oldTool 객체에 값만 덮어쓰기

		// 썸네일 저장
		if (thumbnail != null && !thumbnail.isEmpty()) {
			if (oldTool.getThunbnail() != null) {
				ToolFile oldFile = toolFileRepository.findById(oldTool.getThunbnail()).orElse(null);
				if (oldFile != null) {
					File prefile = new File(toolfileUpload, oldFile.getFileRename());
					if (prefile.exists())
						prefile.delete();
					toolFileRepository.delete(oldFile);
				}
			}
			Integer thumbnailIdx = saveToolFile(thumbnail);
			oldTool.setThunbnail(thumbnailIdx);
		}

		// 상세 이미지 저장
		for (int i = 0; i < imgs.size() && i < 5; i++) {
			MultipartFile img = imgs.get(i);
			if (img == null || img.isEmpty())
				continue;

			// 기존 이미지 삭제
			Integer oldImgIdx = getOldImgIdx(oldTool, i);
			if (oldImgIdx != null) {
				ToolFile oldFile = toolFileRepository.findById(oldImgIdx).orElse(null);
				if (oldFile != null) {
					File prefile = new File(toolfileUpload, oldFile.getFileRename());
					if (prefile.exists())
						prefile.delete();
					toolFileRepository.delete(oldFile);
				}
			}

			// 새 이미지 저장
			Integer newIdx = saveToolFile(img);
			setToolImgIdx(oldTool, i, newIdx);
		}

	}

	//내공구 목록
	@Override
	public ToolCardsMoreDto myTools(String username, Integer toolStatusNo, Integer size, Integer offset)
			throws Exception {
		ToolCardsDto toolCards = toolCardDsl.myTools(username, toolStatusNo, size, offset);

		boolean hasNext = (offset + 1) * size < toolCards.getTotalCount();

		return new ToolCardsMoreDto(toolCards.getCards(), toolCards.getTotalCount(), hasNext);
	}
	
	
	//공구 상태변경
	@Override
	public ToolStatus stopTool(String username, Integer toolIdx) throws Exception {
		System.out.println(toolIdx);
		Tool tool = toolRepository.findById(toolIdx).orElseThrow(()-> new Exception("toolIdx error"));
		
		
		if (!tool.getOwner().equals(username)) {
	        throw new Exception("권한 없음");
	    }
		
		
		if(tool.getSatus() == ToolStatus.ABLE) {
			tool.setSatus(ToolStatus.STOP);
			toolRepository.save(tool);
		
		}else if(tool.getSatus() == ToolStatus.STOP){
			tool.setSatus(ToolStatus.ABLE);
			toolRepository.save(tool);
		}
		
		return tool.getSatus();
	}

	
	//공구 삭제
	@Override
	public ToolStatus delteTool(String username, Integer toolIdx) throws Exception {
		
		Tool tool = toolRepository.findById(toolIdx).orElseThrow(()-> new Exception("toolIdx error"));
		
		
		if (!tool.getOwner().equals(username)) {
	        throw new Exception("권한 없음");
	    }
			
	    tool.setSatus(ToolStatus.DELETE);
	    toolRepository.save(tool);
		
		
		return tool.getSatus();
	}

	
	//대상 상세 
	@Override
	public ToolDetailviewDto targetTool(Integer toolIdx, String username) throws Exception {
		ToolDetailviewDto toolDto = toolCardDsl.toolDetails(toolIdx, username);
		System.out.println(toolDto);
		return toolDto;
	}

	//유저의 다른 공구
	@Override
	public ToolCardsDto ownersTool(String username, String owner, Integer toolIdx) throws Exception {
		
		System.out.println("또 왜");
		ToolCardsDto toolDto = toolCardDsl.toolOwner(username, owner, toolIdx);
		
		return toolDto;
	}

	//공구 리뷰
	@Override
	public Map<String,Object> toolsReview(Integer toolIdx, Integer page, Integer orderNo) throws Exception {
		
		int pageSize = 5; // 한 페이지에 5개
        PageRequest pageRequest = PageRequest.of(page-1, pageSize);
		
		Map<String,Object> toolReview = toolCardDsl.toolReview(toolIdx, pageRequest, orderNo);
		
		return toolReview;
	}

	

	
	

}
