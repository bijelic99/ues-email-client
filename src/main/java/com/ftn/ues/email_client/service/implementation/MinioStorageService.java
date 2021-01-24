package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.configuration.ApplicationConfiguration;
import com.ftn.ues.email_client.model.Attachment;
import com.ftn.ues.email_client.model.AttachmentRaw;
import com.ftn.ues.email_client.service.FileStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.aspectj.bridge.AbortException;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public String addAttachment(AttachmentRaw attachment) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        return addObjectToStorage(attachment, configuration.getMinioAttachmentsBucketName());
    }

    @Override
    public Set<String> addAttachment(Collection<AttachmentRaw> attachments) {
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
    public String addContactPhoto(AttachmentRaw contact) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        return addObjectToStorage(contact, configuration.getMinioContactPhotosBucketName());
    }

    public String addObjectToStorage(AttachmentRaw data, String bucketName) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        var dataId = UUID.randomUUID().toString();
        var extension = "";
        var matcher = Pattern.compile("\\..*$").matcher(data.getFilename());
        while (matcher.find()) {
            extension = matcher.group();
        }
        var fileName = dataId + extension;
        Path tempFile = Files.createTempFile(dataId, extension);
        try (InputStream in = new ByteArrayInputStream(data.getData())) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        UploadObjectArgs.Builder builder = UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .filename(tempFile.toString());
        minioClient.uploadObject(builder.build());

        return fileName;
    }

    @Override
    public List<Pair<Attachment, AttachmentRaw>> getAttachments(Collection<Attachment> attachments) {
        return attachments.stream().map(attachment -> {

            try{
                var objectArgs = GetObjectArgs.builder()
                        .bucket(configuration.getMinioAttachmentsBucketName())
                        .object(attachment.getPath())
                        .build();
                byte[] data;
                try (var res = minioClient.getObject(objectArgs)){
                    try (var outStr = new ByteArrayOutputStream()) {
                        res.transferTo(outStr);
                        data = outStr.toByteArray();
                    }
                }
                return new Pair<>(attachment,
                        new AttachmentRaw(attachment.getName(), attachment.getMimeType(), data));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).collect(Collectors.toList());
    }
}
