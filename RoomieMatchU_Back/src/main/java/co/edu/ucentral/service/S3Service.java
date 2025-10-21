package co.edu.ucentral.service;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import java.util.UUID;

@ApplicationScoped
@Startup
public class S3Service {

    private final S3Client s3;

    @ConfigProperty(name = "app.s3.bucket")
    String bucketName;

    public S3Service(
            @ConfigProperty(name = "quarkus.s3.aws.region") String region,
            @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id") String accessKey,
            @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key") String secretKey
    ) {
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    public String uploadFile(InputStream inputStream, String fileName, String contentType) {
        String uniqueName = UUID.randomUUID() + "_" + fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueName)
                .contentType(contentType)
                .build();

        try {
            byte[] bytes = inputStream.readAllBytes();
            s3.putObject(
                    request,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(bytes)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo para subirlo a S3", e);
        }

        return generatePublicUrl(uniqueName);
    }

    private String generatePublicUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, s3.serviceClientConfiguration().region().id(), key);
    }
}
