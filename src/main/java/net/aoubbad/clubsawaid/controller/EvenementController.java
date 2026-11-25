package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.EvenementPublicationRequest;
import net.aoubbad.clubsawaid.dto.EvenementRegistrationRequest;
import net.aoubbad.clubsawaid.dto.EvenementRequest;
import net.aoubbad.clubsawaid.dto.EvenementResponse;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;
import net.aoubbad.clubsawaid.service.EvenementService;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EvenementController {

    private final EvenementService evenementService;

    @GetMapping
    public ResponseEntity<List<EvenementResponse>> getPublished() {
        return ResponseEntity.ok(evenementService.getEvenementsPublies());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EvenementResponse>> getUpcoming() {
        return ResponseEntity.ok(evenementService.getEvenementsAVenir());
    }

    @GetMapping("/past")
    public ResponseEntity<List<EvenementResponse>> getPast() {
        return ResponseEntity.ok(evenementService.getEvenementsPasses());
    }

    @GetMapping("/next")
    public ResponseEntity<List<EvenementResponse>> getNext(@RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(evenementService.getProchainsEvenements(limit));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EvenementResponse>> getByType(@PathVariable TypeEvenement type) {
        return ResponseEntity.ok(evenementService.getEvenementsByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EvenementResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(evenementService.rechercherEvenements(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvenementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(evenementService.getEvenementById(id));
    }

    @PostMapping
    public ResponseEntity<EvenementResponse> create(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("dateEvenement") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEvenement,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam("lieu") String lieu,
            @RequestParam("capaciteMax") Integer capaciteMax,
            @RequestParam("type") String type,
            @RequestParam("organisateurId") Long organisateurId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        EvenementRequest request = new EvenementRequest();
        request.setTitre(titre);
        request.setDescription(description);
        request.setDateEvenement(dateEvenement);
        request.setDateFin(dateFin);
        request.setLieu(lieu);
        request.setCapaciteMax(capaciteMax);
        request.setType(TypeEvenement.valueOf(type));
        request.setOrganisateurId(organisateurId);

        EvenementResponse response = evenementService.creerEvenement(request, imageFile);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EvenementResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody EvenementRequest request) {
        return ResponseEntity.ok(evenementService.mettreAJourEvenement(id, request));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long id,
                                        @Valid @RequestBody EvenementPublicationRequest request) {
        evenementService.changerPublication(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Void> register(@PathVariable Long id,
                                         @Valid @RequestBody EvenementRegistrationRequest request) {
        evenementService.inscrireMembre(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evenementService.supprimerEvenement(id);
        return ResponseEntity.noContent().build();
    }
}
