package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.*;
import com.ftn.ues.email_client.service.IngestService;
import com.ftn.ues.email_client.service.MailClientService;
import com.ftn.ues.email_client.service.MessageService;
import com.ftn.ues.email_client.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImapIngestService implements IngestService<ImapIngestServiceParams> {

    @Autowired
    MailClientService mailClientService;

    @Autowired
    MessageService messageService;

    @Autowired
    RuleService ruleService;

    @Override
    public void ingestNewMail(ImapIngestServiceParams params) throws MessagingException {
        var newMail = getNewMail(params.getAccount(), params.getFolder());
        var newMessageIds = newMail.stream().map(Identifiable::getId).collect(Collectors.toSet());
        ruleService.executeRules(params.getAccount(), params.getFolder(),newMessageIds);
        messageService.indexMessages(newMessageIds);
    }

    private Set<Message> getNewMail(Account account, Folder folder) throws MessagingException {
        var newMail = mailClientService.getNewImapMessages(account, folder);
        return messageService.saveMessages(account, newMail);
    }
}
