package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Album;
import co.edu.umanizales.appmusic.model.Artist;
import co.edu.umanizales.appmusic.service.AlbumService;
import co.edu.umanizales.appmusic.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para gestionar recursos de Álbumes
@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {
    // Servicio de dominio que maneja la lógica de álbumes
    private final AlbumService albumService;
    private final ArtistService artistService;

    // GET /albums - Lista todos los álbumes
    @GetMapping
    public ResponseEntity<List<Album>> list() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    // GET /albums/{id} - Obtiene un álbum por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<Album> findById(@PathVariable String id) {
        Album album = albumService.getAlbumById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /albums - Crea un nuevo álbum. Requiere un artista válido asociado
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Album album) {
        if (album.getArtist() == null || album.getArtist().getIdArtist() == null || album.getArtist().getIdArtist().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        // Resolver artista por id para evitar campos nulos en relaciones
        Artist artist = artistService.getArtistById(album.getArtist().getIdArtist());
        if (artist == null) {
            return ResponseEntity.badRequest().build();
        }
        album.setArtist(artist);
        albumService.addAlbum(album);
        return ResponseEntity.ok().build();
    }

    // PUT /albums - Actualiza datos de un álbum existente
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Album album) {
        if (album.getArtist() != null && album.getArtist().getIdArtist() != null && !album.getArtist().getIdArtist().isBlank()) {
            Artist artist = artistService.getArtistById(album.getArtist().getIdArtist());
            if (artist == null) {
                return ResponseEntity.badRequest().build();
            }
            album.setArtist(artist);
        }
        albumService.updateAlbum(album);
        return ResponseEntity.ok().build();
    }

    // DELETE /albums/{id} - Elimina un álbum por su identificador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
}
