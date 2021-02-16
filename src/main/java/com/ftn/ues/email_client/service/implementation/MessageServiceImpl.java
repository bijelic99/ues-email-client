package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.model.MessageRaw;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.AttachmentService;
import com.ftn.ues.email_client.service.MailClientService;
import com.ftn.ues.email_client.service.MessageIndexService;
import com.ftn.ues.email_client.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MailClientService mailClientService;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    MessageIndexService messageIndexService;

    @Override
    public Message sendMessage(Message message) throws MessagingException {
        var sentMessage = messageRepository.save(message);
        sentMessage = mailClientService.sendMessage(message);
        sentMessage = messageRepository.save(sentMessage);

        return sentMessage;
    }

    @Override
    public Set<Message> saveMessages(Account account, Set<MessageRaw> rawMessages) {
        return rawMessages.stream()
                .map(rawMessage -> {
                    var message = Message.builder()
                            .messageUid(rawMessage.getId())
                            .from(rawMessage.getFrom())
                            .to(rawMessage.getTo())
                            .cc(rawMessage.getCc())
                            .bcc(rawMessage.getBcc())
                            .dateTime(rawMessage.getDateTime())
                            .unread(rawMessage.getUnread())
                            .subject(rawMessage.getSubject())
                            .content(rawMessage.getContent())
                            .account(account)
                            .build();
                    message = messageRepository.save(message);
                    message.setAttachments(attachmentService.saveAttachments(message, rawMessage.getRawAttachments()));
                    return message;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<com.ftn.ues.email_client.dao.elastic.Message> indexMessages(Set<Long> messageIds) {
        return messageIndexService.index(messageIds);
    }
}
