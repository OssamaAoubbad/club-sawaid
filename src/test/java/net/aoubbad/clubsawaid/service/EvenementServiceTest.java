package net.aoubbad.clubsawaid.service;

import net.aoubbad.clubsawaid.dto.EvenementRegistrationRequest;
import net.aoubbad.clubsawaid.entity.Evenement;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.enums.Role;
import net.aoubbad.clubsawaid.repository.EvenementRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvenementServiceTest {

    @Mock
    private EvenementRepository evenementRepository;

    @Mock
    private MembreRepository membreRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EvenementService evenementService;

    @Test
    void inscrireMembre_throwsWhenEventIsFull() {
        Long evenementId = 99L;
        Long membreId = 17L;

        Evenement evenement = Evenement.builder()
                .id(evenementId)
                .capaciteMax(1)
                .participants(new ArrayList<>())
                .build();

        Membre membre = Membre.builder()
                .id(membreId)
                .nom("Test")
                .prenom("User")
                .email("test@example.com")
                .motDePasse("secret")
                .role(Role.MEMBRE)
                .dateInscription(LocalDate.now())
                .actif(true)
                .emailVerifie(true)
                .build();

        when(evenementRepository.findById(evenementId)).thenReturn(Optional.of(evenement));
        when(membreRepository.findById(membreId)).thenReturn(Optional.of(membre));
        when(evenementRepository.isMembreInscrit(evenementId, membreId)).thenReturn(false);
        when(evenementRepository.countParticipants(evenementId)).thenReturn(1);

        assertThrows(IllegalStateException.class, () ->
                evenementService.inscrireMembre(evenementId, new EvenementRegistrationRequest(membreId))
        );

        verify(evenementRepository, never()).save(any());
    }
}
