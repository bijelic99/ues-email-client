package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
