package net.aoubbad.clubsawaid.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {

    @NotBlank
    private String nom;

    @Email
    @NotBlank
    private String email;

    private String telephone;

    @NotBlank
    private String sujet;

    @NotBlank
    private String message;
}
