package net.aoubbad.clubsawaid.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ImageUploadResponse;
import net.aoubbad.clubsawaid.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final StorageService storageService;

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @NotNull @RequestParam("file") MultipartFile file) throws IOException {
        String url = storageService.store(file);
        return ResponseEntity.ok(new ImageUploadResponse(url));
    }
}
