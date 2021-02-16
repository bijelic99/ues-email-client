package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.*;
import org.javatuples.Pair;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Set;

public interface MessageService {
    Message sendMessage(Message message) throws MessagingException;
    Set<Message> saveMessages(Account account, Set<MessageRaw> rawMessages);
    Set<com.ftn.ues.email_client.dao.elastic.Message> indexMessages(Set<Long> messageIds);
}
