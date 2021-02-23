package com.ftn.ues.email_client.controller;


import com.ftn.ues.email_client.dao.rest.Message;
import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.repository.database.AccountRepository;
import com.ftn.ues.email_client.repository.database.AttachmentRepository;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.FileStorageService;
import com.ftn.ues.email_client.service.MessageIndexService;
import com.ftn.ues.email_client.service.MessageService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageRepository repository;

    @Autowired
    MessageIndexService messageIndexService;

    @GetMapping("/all")
    public List<Message> getAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return DirectMappingConverter.toMapping(repository.findAll(), com.ftn.ues.email_client.model.Message.class, Message.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable("id") Long id) {
        return repository.findById(id).map(Message::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    MessageService messageService;

    @PostMapping
    public ResponseEntity<Boolean> sendMessage(
            @RequestParam(value = "account") Long accountId,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "cc") String cc,
            @RequestParam(value = "bcc") String bcc,
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "subject") String content,
            @RequestParam(value = "attachments") MultipartFile[] attachments
    ) throws MessagingException {
        var acc = accountRepository.findById(accountId).orElseThrow();

        var msg = com.ftn.ues.email_client.model.Message.builder()
                .id(null)
                .from(acc.getMailAddress())
                .to(to)
                .cc(cc)
                .bcc(bcc)
                .subject(subject)
                .content(content)
                .unread(true)
                .account(acc)
                .dateTime(DateTime.now())
                .build();

        msg = messageRepository.save(msg);

        com.ftn.ues.email_client.model.Message finalMsg = msg;
        var atts = Arrays.stream(attachments).map(attachment -> {
            try {
                var araw = new AttachmentRaw(attachment.getOriginalFilename(), attachment.getContentType(), attachment.getBytes());
                var path = fileStorageService.addAttachment(araw);
                var att = Attachment.builder()
                        .id(null)
                        .path(path)
                        .mimeType(attachment.getContentType())
                        .name(attachment.getOriginalFilename())
                        .message(finalMsg)
                        .build();
                return attachmentRepository.save(att);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());

        msg.setAttachments(atts);
        msg = messageRepository.save(msg);

        messageService.sendMessage(msg);

        return ResponseEntity.ok(true);
    }
}
