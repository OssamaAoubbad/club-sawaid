package net.aoubbad.clubsawaid.repository;

import net.aoubbad.clubsawaid.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // --- Messages non lus (pour l'admin) ---
    List<Contact> findByLuFalseOrderByDateEnvoiDesc();

    // --- Tous les messages triés par date ---
    List<Contact> findAllByOrderByDateEnvoiDesc();

    // --- Marquer un message comme lu ---
    @Modifying
    @Query("UPDATE Contact c SET c.lu = true WHERE c.id = :id")
    void marquerCommeLu(@Param("id") Long id);

    // --- Marquer tous les messages comme lus ---
    @Modifying
    @Query("UPDATE Contact c SET c.lu = true WHERE c.lu = false")
    void marquerTousCommeLus();

    // --- Nombre de messages non lus ---
    long countByLuFalse();

    // --- Recherche par email expéditeur ---
    List<Contact> findByEmailOrderByDateEnvoiDesc(String email);
}
