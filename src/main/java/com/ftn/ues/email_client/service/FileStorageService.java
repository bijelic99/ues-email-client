package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.AttachmentRaw;
import io.minio.errors.*;
import org.javatuples.Pair;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FileStorageService {

    String addAttachment(AttachmentRaw attachment) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException;

    Set<String> addAttachment(Collection<AttachmentRaw> attachments);

    String addContactPhoto(AttachmentRaw contact) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    List<Pair<Attachment, AttachmentRaw>> getAttachments(Collection<Attachment> attachments);

}
