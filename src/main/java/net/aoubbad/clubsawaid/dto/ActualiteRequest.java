package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.CategorieActualite;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualiteRequest {

    @NotBlank
    private String titre;

    @NotBlank
    private String contenu;

    private String imageUrl;

    private CategorieActualite categorie;

    @NotNull
    private Long auteurId;
}
