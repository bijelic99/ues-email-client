package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.StoredDataWrapper;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FileStorageService {

    String addAttachment(StoredDataWrapper attachment) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException;

    Set<String> addAttachment(Collection<StoredDataWrapper> attachments);

    String addContactPhoto(StoredDataWrapper contact) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException;

    List<StoredDataWrapper> getAttachments(Collection<Attachment> attachments);

}
