package letsexploretanzania.co.tz.letsexploretanzania.service.common;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AWSService {

    public String saveImageToS3(MultipartFile image, String objectName) throws IOException {


        File tempFile = File.createTempFile("upload-", image.getOriginalFilename());
        image.transferTo(tempFile);


        Path path = tempFile.toPath();
        String bucketName = "letsexploretanzania";
        final String region = "eu-west-2"; // Replace with your region
        S3Client s3Client = S3Client.builder().region(Region.of(region)).build();


        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .contentType(Files.probeContentType(path))
                .build();


        try {

            s3Client.putObject(putObjectRequest, path);
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + objectName;

        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }

    }

}
