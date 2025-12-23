package com.puspo.scalablekafkaapp.demominio.service.impl;

import com.puspo.scalablekafkaapp.demominio.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
public class MinioServiceImpl implements MinioService {
    @Value("${minio.bucketName}")
    private String minioBucketName;

    private final MinioClient minioClient;

    MinioServiceImpl(MinioClient minioClient){
        this.minioClient = minioClient;
    }

    @Override
    public String uploadFile(MultipartFile file){
        //set file name
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        //create PutObjectArgs
        try(InputStream input = file.getInputStream()){
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioBucketName)
                    .object(filename)
                    .stream(input,file.getSize(),-1)
                    .contentType(file.getContentType())
                    .build();
            // upload file to minio
            minioClient.putObject(putObjectArgs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    @Override
    public byte[] downloadFile(String filename) {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(minioBucketName)
                .object(filename)
                .build();
        try(InputStream input = minioClient.getObject(getObjectArgs)){
            return input.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileUrl(String filename){
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioBucketName)
                            .object(filename)
                            .method(Method.GET)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or throw a custom exception
        }
    }
}
