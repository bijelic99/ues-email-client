package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
