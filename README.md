# Image Storage Client Starter

이미지 저장 서버를 쉽게 사용하기 위한 Spring Boot Starter

## 설치

### build.gradle

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.GamJaDo:image-storage-client-starter:1.0.0'
}
```

## 설정

### application.yml

```yaml
image-storage:
  client:
    server-url: [image-storage 서버 주소]
    connect-timeout: 5000      # 연결 타임아웃 (ms), 기본값: 5000
    read-timeout: 30000        # 읽기 타임아웃 (ms), 기본값: 30000
    enabled: true              # 활성화 여부, 기본값: true
```

## 사용법

### 기본 사용

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ImageStorageClient imageStorageClient;

    // 이미지 업로드
    public String uploadImage(MultipartFile file) {
        ImageUploadResult result = imageStorageClient.upload(file);
        return result.getUrl();
    }

    // 이미지 삭제
    public void deleteImage(String imageId) {
        imageStorageClient.delete(imageId);
    }
}
```

### 여러 이미지 업로드

```java
public List<String> uploadImages(List<MultipartFile> files) {
    List<ImageUploadResult> results = imageStorageClient.uploadMultiple(files);
    return results.stream()
            .map(ImageUploadResult::getUrl)
            .toList();
}
```

### 서버 상태 확인

```java
public boolean isServerHealthy() {
    return imageStorageClient.healthCheck();
}
```

## API

### ImageStorageClient

| 메서드 | 설명 | 반환 |
|--------|------|------|
| `upload(MultipartFile file)` | 이미지 업로드 | `ImageUploadResult` |
| `uploadMultiple(List<MultipartFile> files)` | 여러 이미지 업로드 | `List<ImageUploadResult>` |
| `delete(String imageId)` | 이미지 삭제 | `ImageDeleteResult` |
| `healthCheck()` | 서버 상태 확인 | `boolean` |

### ImageUploadResult

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `String` | 이미지 ID (삭제 시 사용) |
| `url` | `String` | 원본 이미지 URL |
| `thumbnailUrl` | `String` | 썸네일 URL |
| `originalName` | `String` | 원본 파일명 |
| `storedName` | `String` | 저장된 파일명 |
| `size` | `long` | 파일 크기 (bytes) |
| `contentType` | `String` | MIME 타입 |
| `uploadedAt` | `LocalDateTime` | 업로드 시간 |

### ImageDeleteResult

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `String` | 이미지 ID |
| `deleted` | `boolean` | 삭제 성공 여부 |
| `message` | `String` | 결과 메시지 |

## 예외 처리

```java
try {
    imageStorageClient.upload(file);
} catch (ImageStorageClientException e) {
    // 업로드 실패 처리
    log.error("이미지 업로드 실패: {}", e.getMessage());
}
```

## 요구사항

- Java 17+
- Spring Boot 3.x

## 라이센스

MIT License
