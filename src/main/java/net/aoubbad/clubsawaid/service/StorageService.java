package net.aoubbad.clubsawaid.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Stocke les fichiers envoyés dans le dossier `uploads/images` et expose leur URL publique.
 */
@Service
public class StorageService {

    private final Path uploadDirectory = Paths.get("uploads/images");

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file provided for upload.");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + filename);
        }

        Files.createDirectories(uploadDirectory);
        String uniqueName = UUID.randomUUID() + "_" + filename;
        Path destination = uploadDirectory.resolve(uniqueName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/images/" + uniqueName;
    }
    public Resource loadAsResource(String filename) throws IOException {
        Path file = uploadDirectory.resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Le fichier n'existe pas : " + filename);
        }
    }
}
