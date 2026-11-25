package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.StatutProjet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetSummaryDto {

    private Long id;
    private String titre;
    private StatutProjet statut;
}
