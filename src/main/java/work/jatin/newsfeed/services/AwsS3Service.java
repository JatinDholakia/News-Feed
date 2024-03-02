package work.jatin.newsfeed.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@Slf4j
public class AwsS3Service {

    public static final String BUCKET_NAME = "work.jatin.newsfeed";
    public static final Duration UPLOAD_URL_EXPIRY_DURATION = Duration.ofMinutes(5);
    public static final Duration DOWNLOAD_URL_EXPIRY_DURATION = Duration.ofHours(1);

    public String createPresignedUrl(String key) {
        try (S3Presigner presigner = S3Presigner.create()) {
            PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(BUCKET_NAME).key(key).build();
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(UPLOAD_URL_EXPIRY_DURATION).putObjectRequest(objectRequest).build();
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        }
    }

    public String createPresignedGetUrl(String key) {
        try (S3Presigner presigner = S3Presigner.create()) {
            GetObjectRequest objectRequest = GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(DOWNLOAD_URL_EXPIRY_DURATION).getObjectRequest(objectRequest).build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        }
    }
}
