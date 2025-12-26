package Personal_Server.Image_Storage_Client_starter.exception;

public class ImageStorageClientException extends RuntimeException {

    public ImageStorageClientException(String message) {
        super(message);
    }

    public ImageStorageClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
