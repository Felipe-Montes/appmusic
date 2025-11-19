package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Song;
import co.edu.umanizales.appmusic.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar recursos de Canciones
@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    // Servicio de dominio que maneja la lógica de canciones
    private final SongService songService;

    // GET /songs - Lista todas las canciones
    @GetMapping
    public ResponseEntity<List<Song>> list() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    // GET /songs/{id} - Obtiene una canción por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<Song> findById(@PathVariable String id) {
        Song song = songService.getSongById(id);
        if (song != null) {
            return ResponseEntity.ok(song);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /songs - Crea una nueva canción
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Song song) {
        songService.addSong(song);
        return ResponseEntity.ok().build();
    }

    // PUT /songs - Actualiza una canción existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Song song) {
        songService.updateSong(song);
        return ResponseEntity.ok().build();
    }

    // DELETE /songs/{id} - Elimina una canción por su identificador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
