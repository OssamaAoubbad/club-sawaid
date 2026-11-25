package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.StatutCandidature;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatureStatutRequest {

    @NotNull
    private StatutCandidature statut;
}
