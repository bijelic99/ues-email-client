package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.*;
import com.ftn.ues.email_client.repository.database.AttachmentRepository;
import com.ftn.ues.email_client.repository.database.FolderRepository;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Override
    public void executeRules(Account account, Set<Long> messagesToExecuteOn) {
        // TODO Implement this
        var messages = messageRepository.findAllById(messagesToExecuteOn);
        var rules = account.getAccountRules();
        for (Rule rule : rules) {
            var ruleHits = new HashSet<Message>();
            var ruleMisses = new ArrayList<Message>();
            for (Message message : messages) {
                switch (rule.getCondition()) {
                    case TO:
                        if (fieldMatches(message.getTo(), rule.getValue())) {
                            ruleHits.add(message);
                            break;
                        }
                    case CC:
                        if (fieldMatches(message.getCc(), rule.getValue())) {
                            ruleHits.add(message);
                            break;
                        }
                    case FROM:
                        if (fieldMatches(message.getFrom(), rule.getValue())) {
                            ruleHits.add(message);
                            break;
                        }
                    case SUBJECT:
                        if (fieldMatches(message.getSubject(), rule.getValue())) {
                            ruleHits.add(message);
                            break;
                        }
                    default:
                        ruleMisses.add(message);
                        break;
                }
            }
            executeRule(account, rule, ruleHits);
            messages = ruleMisses;
        }
        List<Message> leftOverMessages = messages;
        account.getFolders().stream().filter(Folder::getMainInbox).findFirst().ifPresent(folder -> {
            messageRepository.saveAll(leftOverMessages.stream().map(msg -> {
                msg.setParentFolder(folder);
                return msg;
            }).collect(Collectors.toSet()));
        });
    }

    private boolean fieldMatches(String field, String match) {
        return field.toLowerCase().contains(match.toLowerCase());
    }

    public void executeRule(Account account, Rule rule, Set<Message> messages) {
        switch (rule.getOperation()) {
            case DELETE:
                messageRepository.deleteAll(messages);
                break;
            case COPY:
                messages.stream()
                        .map(message -> {
                            try {
                                return (Message) message.clone();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .map(message -> {
                            message.setId(null);
                            message.setParentFolder(rule.getDestinationFolder());
                            List<Attachment> atts = new ArrayList<>(message.getAttachments());
                            message.setAttachments(new HashSet<>());
                            var msg = messageRepository.save(message);
                            atts = atts.stream().map(attachment -> {
                                attachment.setId(null);
                                attachment.setMessage(msg);
                                return attachment;
                            }).collect(Collectors.toList());
                            atts = attachmentRepository.saveAll(atts);
                            message.setAttachments(new HashSet<>(atts));
                            message = messageRepository.save(message);
                            return message;
                        }).collect(Collectors.toSet());
                break;
            case MOVE:
                messageRepository.saveAll(messages.stream().map(message -> {
                    message.setParentFolder(rule.getDestinationFolder());
                    return message;
                }).collect(Collectors.toSet()));

        }
    }

    @Override
    public void executeRules(Account account, Folder folder, Set<Long> messagesToExecuteOn) {

    }
}
