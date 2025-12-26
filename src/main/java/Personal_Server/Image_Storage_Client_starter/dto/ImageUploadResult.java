package Personal_Server.Image_Storage_Client_starter.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageUploadResult {

    private String id;

    private String originalName;

    private String storedName;

    private String url;

    private String thumbnailUrl;

    private long size;

    private String contentType;

    private LocalDateTime uploadedAt;

}
