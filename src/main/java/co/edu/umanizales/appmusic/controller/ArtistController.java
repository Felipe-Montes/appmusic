package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Artist;
import co.edu.umanizales.appmusic.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<Artist>> listar() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> buscarPorId(@PathVariable String id) {
        Artist artist = artistService.getArtistById(id);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody Artist artist) {
        artistService.addArtist(artist);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(@RequestBody Artist artist) {
        artistService.updateArtist(artist);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
}
