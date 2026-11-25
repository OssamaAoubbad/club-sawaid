package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.Role;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembreResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String filiere;
    private String promotion;
    private String photoUrl;
    private Role role;
    private LocalDate dateInscription;
    private boolean actif;
    private boolean emailVerifie;
}
