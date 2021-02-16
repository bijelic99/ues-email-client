package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.*;
import com.ftn.ues.email_client.service.IngestService;
import com.ftn.ues.email_client.service.MailClientService;
import com.ftn.ues.email_client.service.MessageService;
import com.ftn.ues.email_client.service.RuleService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Pop3IngestService implements IngestService<Pop3IngestServiceParams> {

    @Autowired
    MailClientService mailClientService;

    @Autowired
    MessageService messageService;

    @Autowired
    RuleService ruleService;

    @Override
    public void ingestNewMail(Pop3IngestServiceParams params) throws MessagingException {
        var newMail = getNewMail(params.getAccount());
        var newMessageIds = newMail.stream().map(Identifiable::getId).collect(Collectors.toSet());
        ruleService.executeRules(params.getAccount(), newMessageIds);
    }

    private Set<Message> getNewMail(Account account) throws MessagingException {
        var newMail = mailClientService.getNewPop3Messages(account);
        return messageService.saveMessages(account, newMail);
    }

    private Set<Long> indexNewMail(Account account, Set<Message> messages) {
        // TODO fix
        //var indexedMessages = messageService.indexMessages(messages);
        return null;
    }
}
