package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvenementResponse {

    private Long id;
    private String titre;
    private String description;
    private LocalDateTime dateEvenement;
    private LocalDateTime dateFin;
    private String lieu;
    private String imageUrl;
    private Integer capaciteMax;
    private TypeEvenement type;
    private boolean publie;
    private LocalDateTime creeLe;
    private MembreSummaryDto organisateur;
    private List<MembreSummaryDto> participants;
}
