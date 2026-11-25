package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.CategorieActualite;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualiteResponse {

    private Long id;
    private String titre;
    private String contenu;
    private String imageUrl;
    private CategorieActualite categorie;
    private boolean publiee;
    private LocalDateTime datePublication;
    private LocalDateTime dateMiseAJour;
    private MembreSummaryDto auteur;
}
