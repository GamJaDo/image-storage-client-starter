package Personal_Server.Image_Storage_Client_starter.client;

import Personal_Server.Image_Storage_Client_starter.dto.ImageDeleteResult;
import Personal_Server.Image_Storage_Client_starter.dto.ImageUploadResult;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 스토리지 클라이언트
 *
 * 사용 예:
 * <pre>
 * {@code
 * @Service
 * public class MyService {
 *     private final ImageStorageClient imageStorageClient;
 *
 *     public String uploadImage(MultipartFile file) {
 *         return imageStorageClient.upload(file).getUrl();
 *     }
 * }
 * }
 * </pre>
 */
public interface ImageStorageClient {

    /**
     * 이미지 업로드
     * @param file 업로드할 파일
     * @return 업로드 결과 (URL 포함)
     */
    ImageUploadResult upload(MultipartFile file);

    /**
     * 여러 이미지 업로드
     * @param files 업로드할 파일들
     * @return 업로드 결과 목록
     */
    List<ImageUploadResult> uploadMultiple(List<MultipartFile> files);

    /**
     * 이미지 삭제
     * @param imageId 이미지 ID
     * @return 삭제 결과
     */
    ImageDeleteResult delete(String imageId);

    /**
     * 서버 상태 확인
     * @return 서버가 정상이면 true
     */
    boolean healthCheck();
}
