package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.configuration.ApplicationConfiguration;
import com.ftn.ues.email_client.service.FileStorageService;
import com.ftn.ues.email_client.util.JavaxMailMessageToMessageConverter;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MinioStorageService implements FileStorageService {

    @Autowired
    ApplicationConfiguration configuration;

    @Autowired
    MinioClient minioClient;

    @Override
    public String addAttachment(JavaxMailMessageToMessageConverter.AttachmentDataWrapper attachment) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        // TODO handle different extensions
        var attachmentId = UUID.randomUUID().toString();
        var extension = "";
        var matcher = Pattern.compile("\\..*$").matcher(attachment.getFilename());
        while (matcher.find()) {
            extension = matcher.group();
        }
        var fileName = attachmentId + extension;
        Path tempFile = Files.createTempFile(attachmentId, extension);
        try (InputStream in = new ByteArrayInputStream(attachment.getData())) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        UploadObjectArgs.Builder builder = UploadObjectArgs.builder()
                .bucket(configuration.getMinioAttachmentsBucketName())
                .object(fileName)
                .filename(tempFile.toString());
        minioClient.uploadObject(builder.build());

        return fileName;
    }

    @Override
    public Set<String> addAttachment(Collection<JavaxMailMessageToMessageConverter.AttachmentDataWrapper> attachments) {
        return attachments.stream().flatMap(attachment -> {
            var returnOpt = Optional.empty();
            try {
                returnOpt = Optional.of(addAttachment(attachment));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnOpt.stream().map(o -> (String) o);
        }).collect(Collectors.toSet());
    }

    @Override
    public Boolean deleteAttachment(String filename) {
        return null;
    }

    @Override
    public Map<String, Boolean> deleteAttachment(String... filenames) {
        return null;
    }

    @Override
    public String addContactPhoto(JavaxMailMessageToMessageConverter.AttachmentDataWrapper attachment) {
        return null;
    }

    @Override
    public Set<String> addContactPhoto(JavaxMailMessageToMessageConverter.AttachmentDataWrapper... attachments) {
        return null;
    }

    @Override
    public Boolean deleteContactPhoto(String filename) {
        return null;
    }

    @Override
    public Map<String, Boolean> deleteContactPhoto(String... filenames) {
        return null;
    }
}
