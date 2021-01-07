package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Contact;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ContactService {
    Contact addContact(Contact contact, MultipartFile photo) throws NoSuchElementException, IOException, ServerException, InsufficientDataException, NoSuchAlgorithmException, InternalException, InvalidResponseException, XmlParserException, InvalidKeyException, ErrorResponseException;
    Contact update(Contact contact);
    Boolean contact(Long id);
}
