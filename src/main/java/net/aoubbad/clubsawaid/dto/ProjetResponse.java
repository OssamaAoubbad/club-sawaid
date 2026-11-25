package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetResponse {

    private Long id;
    private String titre;
    private String description;
    private String objectifs;
    private String imageUrl;
    private StatutProjet statut;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreBenevoles;
    private LocalDateTime creeLe;
    private MembreSummaryDto responsable;
}
