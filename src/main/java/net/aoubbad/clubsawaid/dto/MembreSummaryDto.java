package net.aoubbad.clubsawaid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.aoubbad.clubsawaid.entity.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembreSummaryDto {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String photoUrl;
    private Role role;
}
