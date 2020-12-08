package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}
