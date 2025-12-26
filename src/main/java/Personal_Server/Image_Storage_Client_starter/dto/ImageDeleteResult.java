package Personal_Server.Image_Storage_Client_starter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDeleteResult {

    private String id;

    private boolean deleted;

    private String message;
}
