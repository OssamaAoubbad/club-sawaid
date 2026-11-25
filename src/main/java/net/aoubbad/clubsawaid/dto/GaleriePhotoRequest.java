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
public class GaleriePhotoRequest {

    @NotBlank
    private String url;

    private String legende;

    private String altText;

    @NotNull
    private Long ajouteParId;

    private Long evenementId;
}
