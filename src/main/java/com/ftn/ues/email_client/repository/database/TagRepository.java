package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
