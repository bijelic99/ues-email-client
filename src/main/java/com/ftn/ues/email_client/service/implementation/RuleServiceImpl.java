package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.repository.database.FolderRepository;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    MessageRepository messageRepository;

    @Override
    public void executeRules(Account account, Set<Long> messagesToExecuteOn) {
        // TODO Implement this
        var messages = messageRepository.findAllById(messagesToExecuteOn);
        var rules = account.getAccountRules();
        account.getFolders().stream()
                .findFirst()
                .stream()
                .forEach(folder -> {
                    var msgs = folder.getMessages();
                    msgs.addAll(messages);
                    folder.setMessages(msgs);
                    folderRepository.save(folder);
                });
    }

    @Override
    public void executeRules(Account account, Folder folder, Set<Long> messagesToExecuteOn) {

    }
}
