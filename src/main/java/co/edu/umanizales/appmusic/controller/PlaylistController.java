package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Playlist;
import co.edu.umanizales.appmusic.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar recursos de Listas de Reproducción (Playlists)
@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    // Servicio de dominio que maneja la lógica de playlists
    private final PlaylistService playlistService;

    // GET /playlists - Lista todas las playlists
    @GetMapping
    public ResponseEntity<List<Playlist>> list() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    // GET /playlists/{id} - Obtiene una playlist por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findById(@PathVariable String id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /playlists - Crea una nueva playlist
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Playlist playlist) {
        playlistService.addPlaylist(playlist);
        return ResponseEntity.ok().build();
    }

    // PUT /playlists - Actualiza una playlist existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Playlist playlist) {
        playlistService.updatePlaylist(playlist);
        return ResponseEntity.ok().build();
    }

    // DELETE /playlists/{id} - Elimina una playlist por su identificador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }
}
