package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.model.Message;

import java.util.List;
import java.util.Set;

public interface AttachmentService {
    Set<Attachment> saveAttachments(Message message, List<AttachmentRaw> rawAttachments);
}
