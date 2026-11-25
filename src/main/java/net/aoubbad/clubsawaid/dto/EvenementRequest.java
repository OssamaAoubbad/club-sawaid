package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvenementRequest {

    @NotBlank
    private String titre;

    private String description;

    @NotNull
    private LocalDateTime dateEvenement;

    private LocalDateTime dateFin;

    @NotBlank
    private String lieu;

    private String imageUrl;

    private Integer capaciteMax;

    private TypeEvenement type;

    @NotNull
    private Long organisateurId;
}
