package com.zipddak.mypage.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.zipddak.entity.Notification;
import com.zipddak.dto.NotificationDto;
import com.zipddak.entity.User;
import com.zipddak.entity.Notification.NotificationType;
import com.zipddak.repository.NotificationRepository;
import com.zipddak.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final FirebaseMessaging firebaseMessaging;
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	// fcmToken 세팅
	@Override
	public void registFcmToken(String username, String fcmToken) throws Exception {
		User user = userRepository.findById(username).orElseThrow(() -> new Exception("사용자 오류"));
		user.setFcmToken(fcmToken);
		userRepository.save(user);
	}

	// 알림 목록 조회
	@Override
	public List<NotificationDto> getNotificationList(String username) throws Exception {
		
		System.out.print(username);
		return notificationRepository.findByRecvUsernameAndConfirmFalse(username).stream()
				.map(notification -> modelMapper.map(notification, NotificationDto.class)).collect(Collectors.toList());
	}

	// 알림 전송
	@Override
	public Boolean sendNotification(NotificationDto notificationDto) throws Exception {
		// username으로 fcmToken 가져오기
		String fcmToken = userRepository.findById(notificationDto.getRecvUsername())
				.orElseThrow(() -> new Exception("받는사람 오류")).getFcmToken();

		if (fcmToken == null || fcmToken.isEmpty()) {
			throw new Exception("Fcm Token 오류");
		}

		// Notification 테이블에 저장
		Notification notification = modelMapper.map(notificationDto, Notification.class);
		notificationRepository.save(notification);

		Message message = null;

		// FCM 전송하기
		if (notificationDto.getType().equals(NotificationType.COMMUNITY)) {
			message = Message.builder().setToken(fcmToken)
					.putData("notificationIdx", notification.getNotificationIdx() + "")
					.putData("type", notification.getType().name()).putData("title", notification.getTitle())
					.putData("content", notification.getContent())
					.putData("sendUsername", notification.getSendUsername())
					.putData("communityIdx", notification.getCommunityIdx() + "").build();
		} else if (notificationDto.getType().equals(NotificationType.ESTIMATE)) {
			message = Message.builder().setToken(fcmToken)
					.putData("notificationIdx", notification.getNotificationIdx() + "")
					.putData("type", notification.getType().name()).putData("title", notification.getTitle())
					.putData("content", notification.getContent())
					.putData("sendUsername", notification.getSendUsername())
					.putData("estimateIdx", notification.getEstimateIdx() + "").build();
		} else if (notificationDto.getType().equals(NotificationType.RENTAL)) {
			message = Message.builder().setToken(fcmToken)
					.putData("notificationIdx", notification.getNotificationIdx() + "")
					.putData("type", notification.getType().name()).putData("title", notification.getTitle())
					.putData("content", notification.getContent())
					.putData("sendUsername", notification.getSendUsername())
					.putData("rentalIdx", notification.getRentalIdx() + "").build();
		} else if (notificationDto.getType().equals(NotificationType.REQUEST)) {
			message = Message.builder().setToken(fcmToken)
					.putData("notificationIdx", notification.getNotificationIdx() + "")
					.putData("type", notification.getType().name()).putData("title", notification.getTitle())
					.putData("content", notification.getContent())
					.putData("sendUsername", notification.getSendUsername())
					.putData("requestIdx", notification.getRequestIdx() + "").build();
		} else if (notificationDto.getType().equals(NotificationType.REVIEW)) {
			message = Message.builder().setToken(fcmToken)
					.putData("notificationIdx", notification.getNotificationIdx() + "")
					.putData("type", notification.getType().name()).putData("title", notification.getTitle())
					.putData("content", notification.getContent())
					.putData("sendUsername", notification.getSendUsername())
					.putData("reviewType", notification.getReviewType())
					.putData("reviewIdx", notification.getReviewIdx() + "").build();
		}

		try {
			firebaseMessaging.send(message);
			return true;
		} catch (FirebaseMessagingException fe) {
			fe.printStackTrace();
			return false;
		}

	}

	// 알림 읽음 처리
	@Override
	public Boolean confirmNotification(Integer notificationIdx) throws Exception {
		Notification notification = notificationRepository.findById(notificationIdx)
				.orElseThrow(() -> new Exception("알림번호 오류"));
		notification.setConfirm(true);
		notificationRepository.save(notification);
		return true;
	}

}
