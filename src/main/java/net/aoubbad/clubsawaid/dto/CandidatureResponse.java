package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.StatutCandidature;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureResponse {

    private Long id;
    private String motivation;
    private StatutCandidature statut;
    private LocalDateTime dateCandidature;
    private LocalDateTime dateReponse;
    private MembreSummaryDto membre;
    private ProjetSummaryDto projet;
}
