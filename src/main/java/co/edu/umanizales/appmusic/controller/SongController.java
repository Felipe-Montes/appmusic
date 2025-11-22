package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Song;
import co.edu.umanizales.appmusic.model.Artist;
import co.edu.umanizales.appmusic.model.Album;
import co.edu.umanizales.appmusic.service.SongService;
import co.edu.umanizales.appmusic.service.ArtistService;
import co.edu.umanizales.appmusic.service.AlbumService;
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
    private final ArtistService artistService;
    private final AlbumService albumService;

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
        // Resolver relaciones por ID si vienen objetos parciales
        if (song.getArtist() != null && song.getArtist().getIdArtist() != null && !song.getArtist().getIdArtist().isBlank()) {
            Artist artist = artistService.getArtistById(song.getArtist().getIdArtist());
            if (artist == null) { return ResponseEntity.badRequest().build(); }
            song.setArtist(artist);
        }
        if (song.getAlbum() != null && song.getAlbum().getId() != null && !song.getAlbum().getId().isBlank()) {
            Album album = albumService.getAlbumById(song.getAlbum().getId());
            if (album == null) { return ResponseEntity.badRequest().build(); }
            song.setAlbum(album);
        }
        songService.addSong(song);
        return ResponseEntity.ok().build();
    }

    // PUT /songs - Actualiza una canción existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Song song) {
        if (song.getArtist() != null && song.getArtist().getIdArtist() != null && !song.getArtist().getIdArtist().isBlank()) {
            Artist artist = artistService.getArtistById(song.getArtist().getIdArtist());
            if (artist == null) { return ResponseEntity.badRequest().build(); }
            song.setArtist(artist);
        }
        if (song.getAlbum() != null && song.getAlbum().getId() != null && !song.getAlbum().getId().isBlank()) {
            Album album = albumService.getAlbumById(song.getAlbum().getId());
            if (album == null) { return ResponseEntity.badRequest().build(); }
            song.setAlbum(album);
        }
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
