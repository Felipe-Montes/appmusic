package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Album;
import co.edu.umanizales.appmusic.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final CsvService csvService;
    private final ArtistService artistService;
    @Value("${storage.albums.path}")
    private String filePath;
    private final List<Album> albums = new ArrayList<>();

    @PostConstruct
    void loadAlbumsFromCsv() {
        List<String[]> rows = csvService.readCsv(filePath);
        albums.clear();
        for (String[] row : rows) {
            if (row == null || row.length < 4) {
                continue;
            }
            String id = row[0] != null ? row[0].trim() : "";
            String title = row[1] != null ? row[1].trim() : "";
            String durationStr = row[2] != null ? row[2].trim() : "0";
            String artistId = row[3] != null ? row[3].trim() : "";
            if (id.isEmpty() || title.isEmpty()) {
                continue;
            }
            double duration = 0;
            try { duration = Double.parseDouble(durationStr); } catch (NumberFormatException ignored) {}
            var artist = artistService.getArtistById(artistId);
            albums.add(new Album(id, title, duration, artist, null));
        }
    }

    public List<Album> getAllAlbums() {
        return albums;
    }

    public Album getAlbumById(String id) {
        for (Album album : albums) {
            if (album.getId().equals(id)) {
                return album;
            }
        }
        return null;
    }

    public void addAlbum(Album album) {
        // Validación de unicidad por id
        for (Album existing : albums) {
            if (existing.getId().equals(album.getId())) {
                throw new DuplicateResourceException("Ya existe un álbum con el mismo id: " + album.getId());
            }
        }
        // Validación de unicidad por título
        for (Album existing : albums) {
            if (existing.getTitle().equalsIgnoreCase(album.getTitle())) {
                throw new DuplicateResourceException("Ya existe un álbum con el mismo título: " + album.getTitle());
            }
        }
        albums.add(album);
        saveAlbumsToCsv();
    }

    public void updateAlbum(Album album) {
        for (Album existing : albums) {
            if (existing.getId().equals(album.getId())) {
                existing.setTitle(album.getTitle());
                existing.setDuration(album.getDuration());
                break;
            }
        }
        saveAlbumsToCsv();
    }

    public void deleteAlbum(String id) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getId().equals(id)) {
                albums.remove(i);
                break;
            }
        }
        saveAlbumsToCsv();
    }

    private void saveAlbumsToCsv() {
        List<String[]> data = new ArrayList<>();
        for (Album a : albums) {
            data.add(new String[]{
                    a.getId(),
                    a.getTitle(),
                    String.valueOf(a.getDuration()),
                    (a.getArtist() != null && a.getArtist().getIdArtist() != null) ? a.getArtist().getIdArtist() : ""
            });
        }
        csvService.writeCsv(filePath, data);
    }
}
