package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.EvenementPublicationRequest;
import net.aoubbad.clubsawaid.dto.EvenementRegistrationRequest;
import net.aoubbad.clubsawaid.dto.EvenementRequest;
import net.aoubbad.clubsawaid.dto.EvenementResponse;
import net.aoubbad.clubsawaid.dto.MembreSummaryDto;
import net.aoubbad.clubsawaid.entity.Evenement;
import net.aoubbad.clubsawaid.entity.Membre;
import net.aoubbad.clubsawaid.entity.enums.TypeEvenement;
import net.aoubbad.clubsawaid.repository.EvenementRepository;
import net.aoubbad.clubsawaid.repository.MembreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final MembreRepository membreRepository;
    private final EmailService emailService;
    private final StorageService storageService;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");

    public List<EvenementResponse> getEvenementsPublies() {
        return evenementRepository.findByPublieTrueOrderByDateEvenementAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EvenementResponse> getEvenementsAVenir() {
        return evenementRepository.findEvenementsAVenir(LocalDateTime.now()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EvenementResponse> getEvenementsPasses() {
        return evenementRepository.findEvenementsPasses(LocalDateTime.now()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EvenementResponse> getProchainsEvenements(int n) {
        return evenementRepository.findProchainsEvenements(LocalDateTime.now(), n).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EvenementResponse> getEvenementsByType(TypeEvenement type) {
        return evenementRepository.findByTypeAndPublieTrue(type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EvenementResponse getEvenementById(Long id) {
        return toResponse(getEvenementEntityById(id));
    }

    public List<EvenementResponse> rechercherEvenements(String keyword) {
        return evenementRepository.searchByKeyword(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EvenementResponse creerEvenement(EvenementRequest request, MultipartFile imageFile) throws IOException {
        Membre organisateur = membreRepository.findById(request.getOrganisateurId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found."));

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = storageService.store(imageFile);
        }

        Evenement evenement = Evenement.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .dateEvenement(request.getDateEvenement())
                .dateFin(request.getDateFin())
                .lieu(request.getLieu())
                .imageUrl(imageUrl)
                .capaciteMax(request.getCapaciteMax())
                .type(request.getType())
                .publie(false)
                .organisateur(organisateur)
                .build();
        System.out.println("event created");

        return toResponse(evenementRepository.save(evenement));
    }

    @Transactional
    public EvenementResponse mettreAJourEvenement(Long id, EvenementRequest request) {
        Evenement evenement = getEvenementEntityById(id);
        evenement.setTitre(request.getTitre());
        evenement.setDescription(request.getDescription());
        evenement.setDateEvenement(request.getDateEvenement());
        evenement.setDateFin(request.getDateFin());
        evenement.setLieu(request.getLieu());
        evenement.setCapaciteMax(request.getCapaciteMax());
        evenement.setType(request.getType());
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            evenement.setImageUrl(request.getImageUrl());
        }
        return toResponse(evenementRepository.save(evenement));
    }

    @Transactional
    public void changerPublication(Long id, EvenementPublicationRequest request) {
        Evenement evenement = getEvenementEntityById(id);
        evenement.setPublie(request.getPublie());
        evenementRepository.save(evenement);
    }

    @Transactional
    public void supprimerEvenement(Long id) {
        if (!evenementRepository.existsById(id)) {
            throw new IllegalArgumentException("Event not found for id: " + id);
        }
        evenementRepository.deleteById(id);
    }

    @Transactional
    public void inscrireMembre(Long evenementId, EvenementRegistrationRequest request) {
        Evenement evenement = getEvenementEntityById(evenementId);
        Membre membre = membreRepository.findById(request.getMembreId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        if (evenementRepository.isMembreInscrit(evenementId, request.getMembreId())) {
            throw new IllegalStateException("You are already registered for this event.");
        }

        if (evenement.getCapaciteMax() != null) {
            int nbParticipants = evenementRepository.countParticipants(evenementId);
            if (nbParticipants >= evenement.getCapaciteMax()) {
                throw new IllegalStateException("This event is full.");
            }
        }

        evenement.getParticipants().add(membre);
        evenementRepository.save(evenement);

        String dateFormatee = evenement.getDateEvenement().format(DATE_FORMAT);
        emailService.envoyerConfirmationInscriptionEvenement(
                membre.getEmail(), membre.getPrenom(),
                evenement.getTitre(), dateFormatee, evenement.getLieu()
        );
    }

    @Transactional
    public void desinscrireMembre(Long evenementId, Long membreId) {
        Evenement evenement = getEvenementEntityById(evenementId);
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        if (!evenementRepository.isMembreInscrit(evenementId, membreId)) {
            throw new IllegalStateException("You are not registered for this event.");
        }

        evenement.getParticipants().remove(membre);
        evenementRepository.save(evenement);
    }

    public List<EvenementResponse> getEvenementsParMembre(Long membreId) {
        return evenementRepository.findByParticipantId(membreId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EvenementResponse> getEvenementsOrganisesParMembre(Long membreId) {
        return evenementRepository.findByOrganisateurId(membreId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Evenement getEvenementEntityById(Long id) {
        return evenementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found for id: " + id));
    }

    private EvenementResponse toResponse(Evenement evenement) {
        List<MembreSummaryDto> participants = evenement.getParticipants() == null
                ? Collections.emptyList()
                : evenement.getParticipants().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());

        return EvenementResponse.builder()
                .id(evenement.getId())
                .titre(evenement.getTitre())
                .description(evenement.getDescription())
                .dateEvenement(evenement.getDateEvenement())
                .dateFin(evenement.getDateFin())
                .lieu(evenement.getLieu())
                .imageUrl(evenement.getImageUrl())
                .capaciteMax(evenement.getCapaciteMax())
                .type(evenement.getType())
                .publie(evenement.isPublie())
                .creeLe(evenement.getCreeLe())
                .organisateur(toSummary(evenement.getOrganisateur()))
                .participants(participants)
                .build();
    }

    private MembreSummaryDto toSummary(Membre membre) {
        if (membre == null) {
            return null;
        }
        return MembreSummaryDto.builder()
                .id(membre.getId())
                .nom(membre.getNom())
                .prenom(membre.getPrenom())
                .email(membre.getEmail())
                .photoUrl(membre.getPhotoUrl())
                .role(membre.getRole())
                .build();
    }
}
