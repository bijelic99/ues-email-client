package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.MailClientService;
import com.ftn.ues.email_client.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MailClientService mailClientService;

    @Override
    public Message sendMessage(Message message) throws MessagingException {
        var sentMessage = messageRepository.save(message);
        sentMessage = mailClientService.sendMessage(message);
        sentMessage = messageRepository.save(sentMessage);

        return sentMessage;
    }
}
