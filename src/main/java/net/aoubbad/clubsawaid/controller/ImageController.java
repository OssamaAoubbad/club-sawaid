package net.aoubbad.clubsawaid.controller;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final StorageService storageService;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Resource file = storageService.loadAsResource(filename);

        // Détecte automatiquement le type MIME
        String contentType;
        try {
            Path path = file.getFile().toPath();
            contentType = Files.probeContentType(path);  // méthode Java NIO
        } catch (IOException e) {
            contentType = URLConnection.guessContentTypeFromName(filename); // fallback
        }

        if (contentType == null) {
            contentType = "application/octet-stream"; // type générique si indétectable
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }
}