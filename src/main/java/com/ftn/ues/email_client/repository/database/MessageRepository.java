package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
