package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.model.Contact;
import com.ftn.ues.email_client.model.Photo;
import com.ftn.ues.email_client.repository.database.ContactRepository;
import com.ftn.ues.email_client.repository.database.PhotoRepository;
import com.ftn.ues.email_client.service.ContactIndexService;
import com.ftn.ues.email_client.service.ContactService;
import com.ftn.ues.email_client.service.FileStorageService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ContactIndexService contactIndexService;

    @Override
    public Contact addContact(Contact contact, MultipartFile photo) throws NoSuchElementException, IOException, ServerException, InsufficientDataException, NoSuchAlgorithmException, InternalException, InvalidResponseException, XmlParserException, InvalidKeyException, ErrorResponseException {
        contact.setPhotos(new HashSet<>());
        var savedContact = contactRepository.save(contact);
        if (photo != null) {
            var photoData = new AttachmentRaw(photo.getOriginalFilename(), photo.getContentType(), photo.getBytes());
            var path = fileStorageService.addContactPhoto(photoData);
            var photoToAddToDb = Photo.builder()
                    .id(null)
                    .contact(savedContact)
                    .path(path)
                    .build();
            photoRepository.save(photoToAddToDb);
        }

        var newContact = contactRepository.getOne(savedContact.getId());
        contactIndexService.index(Collections.singleton(newContact.getId()));
        return newContact;
    }

    @Override
    public Contact putContact(Contact contact, MultipartFile photo) throws NoSuchElementException, IOException, ServerException, InsufficientDataException, NoSuchAlgorithmException, InternalException, InvalidResponseException, XmlParserException, InvalidKeyException, ErrorResponseException {
        if (photo != null) {
            var oldPhotos = contact.getPhotos().stream()
                    .map(p -> {
                        p.setDeleted(true);
                        return p;
                    })
                    .collect(Collectors.toSet());
            photoRepository.saveAll(oldPhotos);
            var photoData = new AttachmentRaw(photo.getOriginalFilename(), photo.getContentType(), photo.getBytes());
            var path = fileStorageService.addContactPhoto(photoData);
            var photoToAddToDb = Photo.builder()
                    .id(null)
                    .contact(contact)
                    .path(path)
                    .build();
            photoRepository.save(photoToAddToDb);
        }
        contactRepository.save(contact);
        var updatedContact = contactRepository.getOne(contact.getId());
        contactIndexService.index(Collections.singleton(updatedContact.getId()));
        return updatedContact;
    }

    @Override
    public Boolean deleteContact(Long id) {
        try {
            var contact = contactRepository.findById(id).orElseThrow();
            contact.setDeleted(true);
            putContact(contact, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Contact> getContact(Long id) {
        return contactRepository.findById(id).map(c -> {
            var photos = c.getPhotos().stream().filter(photo -> !photo.getDeleted()).collect(Collectors.toSet());
            c.setPhotos(photos);
            return c;
        });
    }
}
