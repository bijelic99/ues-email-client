package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
