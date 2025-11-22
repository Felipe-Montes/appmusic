package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Playlist;
import co.edu.umanizales.appmusic.model.User;
import co.edu.umanizales.appmusic.model.Song;
import co.edu.umanizales.appmusic.service.PlaylistService;
import co.edu.umanizales.appmusic.service.UserService;
import co.edu.umanizales.appmusic.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

// Controlador REST para gestionar recursos de Listas de Reproducción (Playlists)
@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    // Servicio de dominio que maneja la lógica de playlists
    private final PlaylistService playlistService;
    private final UserService userService;
    private final SongService songService;

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
        // Resolver usuario por id si viene provisto
        if (playlist.getUser() != null && playlist.getUser().getIdUser() != null && !playlist.getUser().getIdUser().isBlank()) {
            User user = userService.getUserById(playlist.getUser().getIdUser());
            if (user == null) { return ResponseEntity.badRequest().build(); }
            playlist.setUser(user);
        }
        // Resolver canciones por id si vienen provistas
        if (playlist.getSongs() != null && !playlist.getSongs().isEmpty()) {
            List<Song> resolved = new ArrayList<>();
            for (Song s : playlist.getSongs()) {
                if (s != null && s.getId() != null && !s.getId().isBlank()) {
                    Song found = songService.getSongById(s.getId());
                    if (found != null) { resolved.add(found); }
                }
            }
            playlist.setSongs(resolved.isEmpty() ? null : resolved);
        }
        playlistService.addPlaylist(playlist);
        return ResponseEntity.ok().build();
    }

    // PUT /playlists - Actualiza una playlist existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Playlist playlist) {
        if (playlist.getUser() != null && playlist.getUser().getIdUser() != null && !playlist.getUser().getIdUser().isBlank()) {
            User user = userService.getUserById(playlist.getUser().getIdUser());
            if (user == null) { return ResponseEntity.badRequest().build(); }
            playlist.setUser(user);
        }
        if (playlist.getSongs() != null && !playlist.getSongs().isEmpty()) {
            List<Song> resolved = new ArrayList<>();
            for (Song s : playlist.getSongs()) {
                if (s != null && s.getId() != null && !s.getId().isBlank()) {
                    Song found = songService.getSongById(s.getId());
                    if (found != null) { resolved.add(found); }
                }
            }
            playlist.setSongs(resolved.isEmpty() ? null : resolved);
        }
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
