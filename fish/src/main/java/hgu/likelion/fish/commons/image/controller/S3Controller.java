package hgu.likelion.fish.commons.image.controller;

import hgu.likelion.fish.commons.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // S3 버킷 이름

    private final S3Service s3Service;

    // 단일 파일 업로드 처리
    @PostMapping("/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("images") MultipartFile multipartFile) {
        try {
            String uploadUrl = s3Service.uploadFiles(multipartFile, "likelion-study/");
            return ResponseEntity.ok(uploadUrl);
        } catch (Exception e) {
            return ResponseEntity.ok("이미지 업로드에 실패했습니다: " + e.getMessage());
        }
    }

    // 다중 파일 업로드 처리
    @PostMapping("/images")
    public ResponseEntity<String> updateUserImages(@RequestParam("images") MultipartFile[] multipartFiles) {
        StringBuilder uploadUrls = new StringBuilder();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String uploadUrl = s3Service.uploadFiles(multipartFile, "likelion-study/");
                uploadUrls.append(uploadUrl).append("\n");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("이미지 업로드에 실패했습니다: " + e.getMessage());
        }
        return ResponseEntity.ok(uploadUrls.toString());
    }
}
