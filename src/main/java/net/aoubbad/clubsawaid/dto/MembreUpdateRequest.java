package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembreUpdateRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    private String telephone;

    private String filiere;

    private String promotion;

    private String photoUrl;
}
