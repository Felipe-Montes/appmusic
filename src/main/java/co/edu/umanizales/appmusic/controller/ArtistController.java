package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Artist;
import co.edu.umanizales.appmusic.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar recursos de Artistas
@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {
    // Servicio de dominio que maneja la lógica de artistas
    private final ArtistService artistService;

    // GET /artists - Lista todos los artistas
    @GetMapping
    public ResponseEntity<List<Artist>> list() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    // GET /artists/{id} - Obtiene un artista por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable String id) {
        Artist artist = artistService.getArtistById(id);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /artists - Crea un nuevo artista
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Artist artist) {
        artistService.addArtist(artist);
        return ResponseEntity.ok().build();
    }

    // PUT /artists - Actualiza un artista existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Artist artist) {
        artistService.updateArtist(artist);
        return ResponseEntity.ok().build();
    }

    // DELETE /artists/{id} - Elimina un artista por su identificador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
}
