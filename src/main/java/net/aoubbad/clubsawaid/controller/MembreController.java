package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ActivationRequest;
import net.aoubbad.clubsawaid.dto.ChangePasswordRequest;
import net.aoubbad.clubsawaid.dto.MembreResponse;
import net.aoubbad.clubsawaid.dto.MembreUpdateRequest;
import net.aoubbad.clubsawaid.dto.RoleChangeRequest;
import net.aoubbad.clubsawaid.entity.enums.Role;
import net.aoubbad.clubsawaid.service.MembreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MembreController {

    private final MembreService membreService;

    @GetMapping
    public ResponseEntity<List<MembreResponse>> getAll() {
        return ResponseEntity.ok(membreService.getTousMembres());
    }

    @GetMapping("/active")
    public ResponseEntity<List<MembreResponse>> getActive() {
        return ResponseEntity.ok(membreService.getMembresActifs());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<MembreResponse>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(membreService.getMembresParRole(role));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MembreResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(membreService.rechercherMembres(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembreResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(membreService.getMembreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembreResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody MembreUpdateRequest request) {
        return ResponseEntity.ok(membreService.mettreAJourProfil(id, request));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @Valid @RequestBody ChangePasswordRequest request) {
        membreService.changerMotDePasse(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(@PathVariable Long id,
                                           @Valid @RequestBody RoleChangeRequest request) {
        membreService.changerRole(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activation")
    public ResponseEntity<Void> setActive(@PathVariable Long id,
                                          @Valid @RequestBody ActivationRequest request) {
        membreService.activerOuDesactiverMembre(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        membreService.supprimerMembre(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/me")
    public ResponseEntity<MembreResponse> me() {
        return ResponseEntity.ok(membreService.getMe());
    }
}
