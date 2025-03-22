package com.rcs.regulatoryComplianceSystem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AwsService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${bucketName}")
    private String bucketName;

    private static final Logger logger = LoggerFactory.getLogger(AwsService.class);

    // Upload file to S3
    public String uploadFile(MultipartFile file) {
        String fileKey = getFileKey(file);

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize()); // Set content length to avoid memory issues
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, inputStream, metadata);
            amazonS3.putObject(putObjectRequest);
            return amazonS3.getUrl(bucketName, fileKey).toString();
        } catch (IOException e) {
            logger.error("File upload failed due to I/O error", e); // Log the specific error
            throw new RuntimeException("File upload failed due to I/O error", e);
        } catch (Exception e) {
            logger.error("File upload failed", e); // Log the specific error
            throw new RuntimeException("File upload failed", e);
        }
    }

    // Generate a unique file key based on UUID and original file name
    public String getFileKey(MultipartFile file) {
        String fileUUID = UUID.randomUUID().toString(); // Use UUID to create a unique file name
        return "uploads/" + fileUUID + "_" + file.getOriginalFilename();
    }
}
