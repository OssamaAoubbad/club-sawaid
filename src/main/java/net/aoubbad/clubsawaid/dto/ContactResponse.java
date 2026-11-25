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
public class ContactResponse {

    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String sujet;
    private String message;
    private boolean lu;
    private LocalDateTime dateEnvoi;
}
