package Personal_Server.Image_Storage_Client_starter.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "image-storage.client")
public class ImageStorageClientProperties {

    private String serverUrl = "";

    private int connectTimeout = 5000;

    private int readTimeout = 30000;

    private boolean enabled = true;
}
