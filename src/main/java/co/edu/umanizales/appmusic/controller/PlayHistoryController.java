package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.PlayHistory;
import co.edu.umanizales.appmusic.service.PlayHistoryService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/play-history")
@RequiredArgsConstructor
public class PlayHistoryController {
    private final PlayHistoryService playHistoryService;

    @GetMapping
    public ResponseEntity<List<PlayHistory>> listar() {
        return ResponseEntity.ok(playHistoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayHistory> buscarPorId(@PathVariable String id) {
        PlayHistory history = playHistoryService.findById(id);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody @Valid PlayHistory history) {
        playHistoryService.save(history);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        playHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
