package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.repository.database.AttachmentRepository;
import com.ftn.ues.email_client.service.FileStorageService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Collections;

@RestController
@RequestMapping("api/attachment")
public class AttachmentController {

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getAttachment(@PathVariable("id") Long id) {
        return attachmentRepository.findById(id)
                .stream()
                .flatMap(attachment -> fileStorageService.getAttachments(Collections.singletonList(attachment)).stream())
                .map(Pair::getValue1)
                .map(attachmentRaw -> {
                    var outStr = new ByteArrayInputStream(attachmentRaw.getData());
                    var isr = new InputStreamResource(outStr);
                    return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachmentRaw.getMimeType())).body(isr);
                })
                .findFirst()
                .orElse(ResponseEntity.notFound().build());
    }
}
