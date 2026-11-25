package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.CandidatureRequest;
import net.aoubbad.clubsawaid.dto.CandidatureResponse;
import net.aoubbad.clubsawaid.dto.CandidatureStatutRequest;
import net.aoubbad.clubsawaid.service.CandidatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
@RequiredArgsConstructor
public class CandidatureController {

    private final CandidatureService candidatureService;

    @PostMapping
    public ResponseEntity<CandidatureResponse> apply(@Valid @RequestBody CandidatureRequest request) {
        return ResponseEntity.ok(candidatureService.postuler(request));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<CandidatureResponse>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesParMembre(memberId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<CandidatureResponse>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesParProjet(projectId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CandidatureResponse>> getPending() {
        return ResponseEntity.ok(candidatureService.getCandidaturesEnAttente());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CandidatureResponse> changeStatus(@PathVariable Long id,
                                                            @Valid @RequestBody CandidatureStatutRequest request) {
        return ResponseEntity.ok(candidatureService.changerStatut(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @RequestParam Long memberId) {
        candidatureService.annulerCandidature(id, memberId);
        return ResponseEntity.noContent().build();
    }
}
