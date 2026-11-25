package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ActualitePublicationRequest;
import net.aoubbad.clubsawaid.dto.ActualiteRequest;
import net.aoubbad.clubsawaid.dto.ActualiteResponse;
import net.aoubbad.clubsawaid.service.ActualiteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class ActualiteController {

    private final ActualiteService actualiteService;

    @GetMapping
    public ResponseEntity<Page<ActualiteResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(actualiteService.getActualitesPubliees(pageable));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ActualiteResponse>> getLatest(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(actualiteService.getDernieresActualites(limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ActualiteResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(actualiteService.rechercherActualites(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActualiteResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(actualiteService.getActualiteById(id));
    }

    @PostMapping
    public ResponseEntity<ActualiteResponse> create(@Valid @RequestBody ActualiteRequest request) {
        return ResponseEntity.ok(actualiteService.creerActualite(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActualiteResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody ActualiteRequest request) {
        return ResponseEntity.ok(actualiteService.mettreAJourActualite(id, request));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long id,
                                        @Valid @RequestBody ActualitePublicationRequest request) {
        actualiteService.changerPublication(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        actualiteService.supprimerActualite(id);
        return ResponseEntity.noContent().build();
    }
}
