package com.pacman.rabbitDemo.repository;

import com.pacman.rabbitDemo.entity.ReceivedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivedMessageRepository extends JpaRepository <ReceivedMessage,Long> {
}
