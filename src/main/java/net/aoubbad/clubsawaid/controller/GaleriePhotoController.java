package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.GaleriePhotoRequest;
import net.aoubbad.clubsawaid.dto.GaleriePhotoResponse;
import net.aoubbad.clubsawaid.service.GaleriePhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class GaleriePhotoController {

    private final GaleriePhotoService galeriePhotoService;

    @GetMapping
    public ResponseEntity<List<GaleriePhotoResponse>> getAll() {
        return ResponseEntity.ok(galeriePhotoService.getToutesLesPhotos());
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<List<GaleriePhotoResponse>> getByEvent(@PathVariable Long id) {
        return ResponseEntity.ok(galeriePhotoService.getPhotosParEvenement(id));
    }

    @GetMapping("/general")
    public ResponseEntity<List<GaleriePhotoResponse>> getGeneral() {
        return ResponseEntity.ok(galeriePhotoService.getPhotosGenerales());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<GaleriePhotoResponse>> latest(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(galeriePhotoService.getDernieresPhotos(limit));
    }

    @PostMapping
    public ResponseEntity<GaleriePhotoResponse> add(@Valid @RequestBody GaleriePhotoRequest request) {
        return ResponseEntity.ok(galeriePhotoService.ajouterPhoto(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestParam Long requesterId,
                                       @RequestParam(defaultValue = "false") boolean admin) {
        galeriePhotoService.supprimerPhoto(id, requesterId, admin);
        return ResponseEntity.noContent().build();
    }
}
