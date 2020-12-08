package com.ftn.ues.email_client.repository.database;

import com.ftn.ues.email_client.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
