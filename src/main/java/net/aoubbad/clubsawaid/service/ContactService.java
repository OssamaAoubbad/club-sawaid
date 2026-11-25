package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.ContactRequest;
import net.aoubbad.clubsawaid.dto.ContactResponse;
import net.aoubbad.clubsawaid.entity.Contact;
import net.aoubbad.clubsawaid.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    @Transactional
    public ContactResponse envoyerMessage(ContactRequest request) {
        Contact contact = Contact.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .sujet(request.getSujet())
                .message(request.getMessage())
                .lu(false)
                .build();

        return toResponse(contactRepository.save(contact));
    }

    public List<ContactResponse> getTousLesMessages() {
        return contactRepository.findAllByOrderByDateEnvoiDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ContactResponse> getMessagesNonLus() {
        return contactRepository.findByLuFalseOrderByDateEnvoiDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ContactResponse getMessageById(Long id) {
        return toResponse(contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found for id: " + id)));
    }

    @Transactional
    public void marquerCommeLu(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new IllegalArgumentException("Message not found for id: " + id);
        }
        contactRepository.marquerCommeLu(id);
    }

    @Transactional
    public void marquerTousCommeLus() {
        contactRepository.marquerTousCommeLus();
    }

    @Transactional
    public void supprimerMessage(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new IllegalArgumentException("Message not found for id: " + id);
        }
        contactRepository.deleteById(id);
    }

    public long countMessagesNonLus() {
        return contactRepository.countByLuFalse();
    }

    private ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .nom(contact.getNom())
                .email(contact.getEmail())
                .telephone(contact.getTelephone())
                .sujet(contact.getSujet())
                .message(contact.getMessage())
                .lu(contact.isLu())
                .dateEnvoi(contact.getDateEnvoi())
                .build();
    }
}
