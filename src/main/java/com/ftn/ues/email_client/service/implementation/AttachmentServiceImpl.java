package com.ftn.ues.email_client.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.repository.database.AttachmentRepository;
import com.ftn.ues.email_client.repository.elastic_search.AttachmentESRepository;
import com.ftn.ues.email_client.service.AttachmentService;
import com.ftn.ues.email_client.service.FileStorageService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
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
    AttachmentESRepository attachmentESRepository;

    @Autowired
    RestHighLevelClient client;

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

    @Override
    public Set<com.ftn.ues.email_client.dao.elastic_search.Attachment> indexAttachments(Collection<Attachment> attachments) {
        List<com.ftn.ues.email_client.dao.elastic_search.Attachment> atts = fileStorageService.getAttachments(attachments).stream()
                .map(com.ftn.ues.email_client.dao.elastic_search.Attachment::new)
                .collect(Collectors.toList());
        HashSet<com.ftn.ues.email_client.dao.elastic_search.Attachment> attList = new HashSet<>();

        atts.stream().forEach(attachment -> {
            try {
                var indexRequest = new IndexRequest("attachment");
                indexRequest.setPipeline("attachment");
                indexRequest.source(objectMapper.writeValueAsString(attachment).getBytes(StandardCharsets.UTF_8), XContentType.JSON);
                client.index(indexRequest, RequestOptions.DEFAULT);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
         return attList;
    }
}
