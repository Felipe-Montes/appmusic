package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.PlayHistory;
import co.edu.umanizales.appmusic.model.User;
import co.edu.umanizales.appmusic.model.Song;
import co.edu.umanizales.appmusic.service.PlayHistoryService;
import co.edu.umanizales.appmusic.service.UserService;
import co.edu.umanizales.appmusic.service.SongService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar el historial de reproducciones (Play History)
@RestController
@RequestMapping("/play-history")
@RequiredArgsConstructor
public class PlayHistoryController {
    // Servicio de dominio que maneja la lógica del historial de reproducciones
    private final PlayHistoryService playHistoryService;
    private final UserService userService;
    private final SongService songService;

    // GET /play-history - Lista todos los registros del historial
    @GetMapping
    public ResponseEntity<List<PlayHistory>> list() {
        return ResponseEntity.ok(playHistoryService.findAll());
    }

    // GET /play-history/{id} - Obtiene un registro del historial por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<PlayHistory> findById(@PathVariable String id) {
        PlayHistory history = playHistoryService.findById(id);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /play-history - Crea un nuevo registro en el historial
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid PlayHistory history) {
        if (history.getUser() != null && history.getUser().getIdUser() != null && !history.getUser().getIdUser().isBlank()) {
            User user = userService.getUserById(history.getUser().getIdUser());
            if (user == null) { return ResponseEntity.badRequest().build(); }
            history.setUser(user);
        }
        if (history.getSong() != null && history.getSong().getId() != null && !history.getSong().getId().isBlank()) {
            Song song = songService.getSongById(history.getSong().getId());
            if (song == null) { return ResponseEntity.badRequest().build(); }
            history.setSong(song);
        }
        playHistoryService.save(history);
        return ResponseEntity.ok().build();
    }

    // DELETE /play-history/{id} - Elimina un registro del historial por su identificador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        playHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
