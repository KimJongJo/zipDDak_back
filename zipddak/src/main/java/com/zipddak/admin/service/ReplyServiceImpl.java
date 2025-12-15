package com.zipddak.admin.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.zipddak.admin.dto.ReplyDetailDto;
import com.zipddak.admin.dto.ResponseReplyListAndHasnext;
import com.zipddak.admin.repository.ReplyDslRepository;
import com.zipddak.entity.Reply;
import com.zipddak.repository.ReplyRepository;
import com.zipddak.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
	
	private final ReplyRepository replyRepository;
	
	private final ReplyDslRepository replyDslRepository;
	
	@Override
	public long replyCountByCommunityId(int communityId) throws Exception {
		
		return replyRepository.countByCommunityIdx(communityId);
	}

	@Override
	public ResponseReplyListAndHasnext replayList(int communityId, int page) {

		PageInfo pageInfo = new PageInfo(page);
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5);
		
		return replyDslRepository.replyList(communityId, pageRequest);
	}

	@Override
	public void writeReply(String username, String content, Integer communityId) throws Exception {

		Reply reply = Reply.builder()
						.writer(username)
						.context(content)
						.communityIdx(communityId)
						.build();
		
		replyRepository.save(reply);
		
	}

	@Override
	public ResponseReplyListAndHasnext replyMore(Integer communityId, Integer page) throws Exception {
		
		PageInfo pageInfo = new PageInfo(page);
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 5);
		
		return replyDslRepository.replyList(communityId, pageRequest);
	}

	@Override
	public void deleteReply(Integer replyId) throws Exception {
		
		Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new Exception("댓글 조회중 오류"));
		
		replyRepository.delete(reply);
		
	}

}
