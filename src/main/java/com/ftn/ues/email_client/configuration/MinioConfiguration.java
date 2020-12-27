package com.ftn.ues.email_client.configuration;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Log4j2
public class MinioConfiguration {

    @Bean
    public MinioClient minIoClient(Environment environment, ApplicationConfiguration applicationConfiguration) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        var accessKey = environment.getRequiredProperty("storage.minio.access-key");
        var secretKey = environment.getRequiredProperty("storage.minio.secret-key");

        var client =  MinioClient.builder()
                .endpoint(applicationConfiguration.getMinioEndpoint())
                .credentials(accessKey, secretKey)
                .build();

        if(!client.bucketExists(BucketExistsArgs.builder().bucket(applicationConfiguration.getMinioAttachmentsBucketName()).build())){
            client.makeBucket(MakeBucketArgs.builder().bucket(applicationConfiguration.getMinioAttachmentsBucketName()).build());
        }
        if(!client.bucketExists(BucketExistsArgs.builder().bucket(applicationConfiguration.getMinioContactPhotosBucketName()).build())){
            client.makeBucket(MakeBucketArgs.builder().bucket(applicationConfiguration.getMinioContactPhotosBucketName()).build());
        }

        return client;
    }

}
