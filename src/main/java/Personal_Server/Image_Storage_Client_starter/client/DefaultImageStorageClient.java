package Personal_Server.Image_Storage_Client_starter.client;

import Personal_Server.Image_Storage_Client_starter.client.config.ImageStorageClientProperties;
import Personal_Server.Image_Storage_Client_starter.dto.ImageDeleteResult;
import Personal_Server.Image_Storage_Client_starter.dto.ImageUploadResult;
import Personal_Server.Image_Storage_Client_starter.exception.ImageStorageClientException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class DefaultImageStorageClient implements ImageStorageClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public DefaultImageStorageClient(RestTemplate restTemplate, ImageStorageClientProperties properties) {
        this.restTemplate = restTemplate;
        this.baseUrl = properties.getServerUrl().replaceAll("/$", "");
    }

    @Override
    public ImageUploadResult upload(MultipartFile file) {
        log.debug("Uploading file: {}", file.getOriginalFilename());

        try {
            // multipart/form-data 요청 만들기
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartFileResource(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImageUploadResult> response = restTemplate.exchange(
                baseUrl + "/api/images",
                HttpMethod.POST,
                requestEntity,
                ImageUploadResult.class
            );

            log.info("Image uploaded successfully: {}", response.getBody().getUrl());
            return response.getBody();

        } catch (RestClientException e) {
            log.error("Failed to upload image", e);
            throw new ImageStorageClientException("Failed to upload image: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("Failed to read file", e);
            throw new ImageStorageClientException("Failed to read file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ImageUploadResult> uploadMultiple(List<MultipartFile> files) {
        log.debug("Uploading {} files", files.size());

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            for (MultipartFile file : files) {
                body.add("files", new MultipartFileResource(file));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImageUploadResult[]> response = restTemplate.exchange(
                baseUrl + "/api/images/batch",
                HttpMethod.POST,
                requestEntity,
                ImageUploadResult[].class
            );

            log.info("Uploaded {} images", response.getBody().length);
            return Arrays.asList(response.getBody());

        } catch (RestClientException e) {
            log.error("Failed to upload images", e);
            throw new ImageStorageClientException("Failed to upload images: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("Failed to read files", e);
            throw new ImageStorageClientException("Failed to read files: " + e.getMessage(), e);
        }
    }

    @Override
    public ImageDeleteResult delete(String imageId) {
        log.debug("Deleting image: {}", imageId);

        try {
            ResponseEntity<ImageDeleteResult> response = restTemplate.exchange(
                baseUrl + "/api/images/" + imageId,
                HttpMethod.DELETE,
                null,
                ImageDeleteResult.class
            );

            log.info("Image deleted: {}", imageId);
            return response.getBody();

        } catch (RestClientException e) {
            log.error("Failed to delete image: {}", imageId, e);
            throw new ImageStorageClientException("Failed to delete image: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean healthCheck() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                baseUrl + "/api/images/health",
                Map.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("Health check failed", e);
            return false;
        }
    }

    private static class MultipartFileResource extends ByteArrayResource {
        private final String filename;

        public MultipartFileResource(MultipartFile file) throws IOException {
            super(file.getBytes());
            this.filename = file.getOriginalFilename();
        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }
}
