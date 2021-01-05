package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.util.JavaxMailMessageToMessageConverter;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FileStorageService {

    String addAttachment(JavaxMailMessageToMessageConverter.AttachmentDataWrapper attachment) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException;

    Set<String> addAttachment(Collection<JavaxMailMessageToMessageConverter.AttachmentDataWrapper> attachments);

    Boolean deleteAttachment(String filename);

    Map<String, Boolean> deleteAttachment(String... filenames);

    String addContactPhoto(JavaxMailMessageToMessageConverter.AttachmentDataWrapper attachment);

    Set<String> addContactPhoto(JavaxMailMessageToMessageConverter.AttachmentDataWrapper... attachments);

    Boolean deleteContactPhoto(String filename);

    Map<String, Boolean> deleteContactPhoto(String... filenames);

    List<JavaxMailMessageToMessageConverter.AttachmentDataWrapper> getAttachments(Collection<Attachment> attachments);

}
