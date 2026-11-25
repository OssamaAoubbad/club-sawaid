package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentaireResponse {

    private Long id;
    private String contenu;
    private LocalDateTime dateCommentaire;
    private Long auteurId;
    private Long evenementId;
    private Long actualiteId;
}
