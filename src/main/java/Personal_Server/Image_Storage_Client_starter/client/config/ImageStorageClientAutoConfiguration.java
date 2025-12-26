package Personal_Server.Image_Storage_Client_starter.client.config;

import Personal_Server.Image_Storage_Client_starter.client.DefaultImageStorageClient;
import Personal_Server.Image_Storage_Client_starter.client.ImageStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(ImageStorageClientProperties.class)
@ConditionalOnProperty(
    prefix = "image-storage.client",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class ImageStorageClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "imageStorageRestTemplate")
    public RestTemplate imageStorageRestTemplate(
        RestTemplateBuilder builder,
        ImageStorageClientProperties properties) {

        log.info("Creating RestTemplate for Image Storage Client (serverUrl: {})", properties.getServerUrl());

        return builder
            .connectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
            .readTimeout(Duration.ofMillis(properties.getReadTimeout()))
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(ImageStorageClient.class)
    public ImageStorageClient imageStorageClient(
        RestTemplate imageStorageRestTemplate,
        ImageStorageClientProperties properties) {

        log.info("Creating ImageStorageClient (serverUrl: {})", properties.getServerUrl());
        return new DefaultImageStorageClient(imageStorageRestTemplate, properties);
    }
}
