package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
