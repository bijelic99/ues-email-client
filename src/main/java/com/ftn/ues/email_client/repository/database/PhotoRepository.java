package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
