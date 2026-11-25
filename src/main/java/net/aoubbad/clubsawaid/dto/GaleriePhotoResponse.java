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
public class GaleriePhotoResponse {

    private Long id;
    private String url;
    private String legende;
    private String altText;
    private LocalDateTime dateAjout;
    private Long evenementId;
    private MembreSummaryDto ajoutePar;
}
