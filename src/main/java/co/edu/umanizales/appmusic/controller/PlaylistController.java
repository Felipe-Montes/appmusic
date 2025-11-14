package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Playlist;
import co.edu.umanizales.appmusic.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<Playlist>> listar() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> buscarPorId(@PathVariable String id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody Playlist playlist) {
        playlistService.addPlaylist(playlist);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(@RequestBody Playlist playlist) {
        playlistService.updatePlaylist(playlist);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }
}
