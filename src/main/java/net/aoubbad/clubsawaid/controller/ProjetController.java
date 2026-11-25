package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ProjetRequest;
import net.aoubbad.clubsawaid.dto.ProjetResponse;
import net.aoubbad.clubsawaid.dto.ProjetStatutRequest;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;
import net.aoubbad.clubsawaid.service.ProjetService;
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
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    @GetMapping
    public ResponseEntity<List<ProjetResponse>> getAll() {
        return ResponseEntity.ok(projetService.getTousProjets());
    }

    @GetMapping("/status/{statut}")
    public ResponseEntity<List<ProjetResponse>> getByStatus(@PathVariable StatutProjet statut) {
        return ResponseEntity.ok(projetService.getProjetsParStatut(statut));
    }

    @GetMapping("/recruiting")
    public ResponseEntity<List<ProjetResponse>> getRecruiting() {
        return ResponseEntity.ok(projetService.getProjetsEnRecrutement());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjetResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(projetService.rechercherProjets(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(projetService.getProjetById(id));
    }

    @PostMapping
    public ResponseEntity<ProjetResponse> create(@Valid @RequestBody ProjetRequest request) {
        return ResponseEntity.ok(projetService.creerProjet(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody ProjetRequest request) {
        return ResponseEntity.ok(projetService.mettreAJourProjet(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id,
                                             @Valid @RequestBody ProjetStatutRequest request) {
        projetService.changerStatut(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetService.supprimerProjet(id);
        return ResponseEntity.noContent().build();
    }
}
