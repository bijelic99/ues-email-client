package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Message;

import javax.mail.MessagingException;

public interface MessageService {
    Message sendMessage(Message message) throws MessagingException;
}
