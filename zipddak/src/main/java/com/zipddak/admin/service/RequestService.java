package com.zipddak.admin.service;

import com.zipddak.admin.dto.RequestFormDto;

public interface RequestService {

	void writeRequest(RequestFormDto requestForm) throws Exception;

}
