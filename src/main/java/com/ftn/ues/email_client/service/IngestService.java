package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.IngestServiceParams;

import javax.mail.MessagingException;

public interface IngestService<T extends IngestServiceParams> {
     void ingestNewMail(T params) throws MessagingException;
}
