package com.ftn.ues.email_client.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.repository.database.AttachmentRepository;
import com.ftn.ues.email_client.service.AttachmentService;
import com.ftn.ues.email_client.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    FileStorageService storageService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Set<Attachment> saveAttachments(Message message, List<AttachmentRaw> rawAttachments) {
        List<Attachment> attachments = rawAttachments.stream()
                .map(attachmentRaw -> {
                    try {
                        var path = storageService.addAttachment(attachmentRaw);
                        return Attachment.builder()
                                .name(attachmentRaw.getFilename())
                                .path(path)
                                .mimeType(attachmentRaw.getMimeType())
                                .message(message)
                                .deleted(false)
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        attachments = attachmentRepository.saveAll(attachments);
        return new HashSet<>(attachments);
    }
}
