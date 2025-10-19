package co.edu.ucentral.storage;

import io.quarkus.runtime.configuration.ConfigurationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@ApplicationScoped
public class S3Uploader {

    private final S3Client s3Client;

    @ConfigProperty(name = "aws.s3.bucket-name")
    String bucketName;

    @Inject
    public S3Uploader(
            @ConfigProperty(name = "aws.access-key-id") String accessKey,
            @ConfigProperty(name = "aws.secret-access-key") String secretKey,
            @ConfigProperty(name = "aws.region") String region
    ) {
        if (accessKey == null || secretKey == null) {
            throw new ConfigurationException("Credenciales AWS no configuradas");
        }

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    public String uploadFile(Path filePath, String key) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(request, filePath);
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}