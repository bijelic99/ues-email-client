package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Contact;
import com.ftn.ues.email_client.model.Photo;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.repository.database.ContactRepository;
import com.ftn.ues.email_client.repository.database.PhotoRepository;
import com.ftn.ues.email_client.service.ContactService;
import com.ftn.ues.email_client.service.FileStorageService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.NoSuchElementException;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Override
    public Contact addContact(Contact contact, MultipartFile photo) throws NoSuchElementException, IOException, ServerException, InsufficientDataException, NoSuchAlgorithmException, InternalException, InvalidResponseException, XmlParserException, InvalidKeyException, ErrorResponseException {
        contact.setPhotos(new HashSet<>());
        var savedContact = contactRepository.save(contact);
        if(photo != null) {
            var photoData = new AttachmentRaw(photo.getOriginalFilename(), photo.getContentType(), photo.getBytes());
            var path = fileStorageService.addContactPhoto(photoData);
            var photoToAddToDb = Photo.builder()
                    .id(null)
                    .contact(savedContact)
                    .path(path)
                    .build();
            photoRepository.save(photoToAddToDb);
        }

        return contactRepository.getOne(savedContact.getId());
    }

    @Override
    public Contact update(Contact contact) {
        return null;
    }

    @Override
    public Boolean contact(Long id) {
        try{
            var contact = contactRepository.findById(id).orElseThrow();
            contact.setDeleted(true);
            contactRepository.save(contact);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
