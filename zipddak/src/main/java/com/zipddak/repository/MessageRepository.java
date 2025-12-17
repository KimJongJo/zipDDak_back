package com.zipddak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zipddak.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
