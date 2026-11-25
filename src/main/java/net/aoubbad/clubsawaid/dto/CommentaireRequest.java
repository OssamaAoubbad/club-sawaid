package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentaireRequest {

    @NotNull
    private Long auteurId;

    @NotBlank
    private String contenu;

    private Long evenementId;

    private Long actualiteId;
}
