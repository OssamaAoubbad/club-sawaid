package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.CommentaireRequest;
import net.aoubbad.clubsawaid.dto.CommentaireResponse;
import net.aoubbad.clubsawaid.service.CommentaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentaireController {

    private final CommentaireService commentaireService;

    @PostMapping("/event")
    public ResponseEntity<CommentaireResponse> commentEvent(@Valid @RequestBody CommentaireRequest request) {
        return ResponseEntity.ok(commentaireService.commenterEvenement(request));
    }

    @PostMapping("/news")
    public ResponseEntity<CommentaireResponse> commentNews(@Valid @RequestBody CommentaireRequest request) {
        return ResponseEntity.ok(commentaireService.commenterActualite(request));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentaireResponse>> getEventComments(@PathVariable Long eventId) {
        return ResponseEntity.ok(commentaireService.getCommentairesEvenement(eventId));
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<List<CommentaireResponse>> getNewsComments(@PathVariable Long newsId) {
        return ResponseEntity.ok(commentaireService.getCommentairesActualite(newsId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId,
                                       @RequestParam Long requesterId,
                                       @RequestParam(defaultValue = "false") boolean admin) {
        commentaireService.supprimerCommentaire(commentId, requesterId, admin);
        return ResponseEntity.noContent().build();
    }
}
