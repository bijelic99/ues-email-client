package com.ftn.ues.email_client.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApplicationConfiguration {

    @Value("${storage.minio.endpoint}")
    private String minioEndpoint;

    @Value("${storage.minio.attachments-bucket:attachments}")
    private String minioAttachmentsBucketName;

    @Value("${storage.minio.contact-photos-bucket:contact-photos}")
    private String minioContactPhotosBucketName;

}
