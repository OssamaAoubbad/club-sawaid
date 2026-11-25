package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetRequest {

    @NotBlank
    private String titre;

    private String description;

    private String objectifs;

    private String imageUrl;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Integer nombreBenevoles;

    private StatutProjet statut;

    @NotNull
    private Long responsableId;
}
