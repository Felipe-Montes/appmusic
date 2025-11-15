package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Artist;
import co.edu.umanizales.appmusic.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final CsvService csvService;
    @Value("${storage.artists.path}")
    private String filePath;
    private final List<Artist> artists = new ArrayList<>();

    @PostConstruct
    void loadArtistsFromCsv() {
        List<String[]> rows = csvService.readCsv(filePath);
        artists.clear();
        for (String[] row : rows) {
            if (row == null || row.length < 2) {
                continue;
            }
            String id = row[0] != null ? row[0].trim() : "";
            String name = row[1] != null ? row[1].trim() : "";
            if (id.isEmpty() || name.isEmpty()) {
                continue;
            }
            artists.add(new Artist(id, name, null, null));
        }
    }

    public List<Artist> getAllArtists() {
        return artists;
    }

    public Artist getArtistById(String id) {
        for (Artist artist : artists) {
            if (artist.getIdArtist().equals(id)) {
                return artist;
            }
        }
        return null;
    }

    public void addArtist(Artist artist) {
        // Validación de unicidad por id
        for (Artist existing : artists) {
            if (existing.getIdArtist().equals(artist.getIdArtist())) {
                throw new DuplicateResourceException("Ya existe un artista con el mismo id: " + artist.getIdArtist());
            }
        }
        // Validación de unicidad por nombre
        for (Artist existing : artists) {
            if (existing.getName().equalsIgnoreCase(artist.getName())) {
                throw new DuplicateResourceException("Ya existe un artista con el mismo nombre: " + artist.getName());
            }
        }
        artists.add(artist);
        saveArtistsToCsv();
    }

    public void updateArtist(Artist artist) {
        for (Artist existing : artists) {
            if (existing.getIdArtist().equals(artist.getIdArtist())) {
                existing.setName(artist.getName());
                break;
            }
        }
        saveArtistsToCsv();
    }

    public void deleteArtist(String id) {
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getIdArtist().equals(id)) {
                artists.remove(i);
                break;
            }
        }
        saveArtistsToCsv();
    }

    private void saveArtistsToCsv() {
        List<String[]> data = new ArrayList<>();
        for (Artist a : artists) {
            data.add(new String[]{a.getIdArtist(), a.getName()});
        }
        csvService.writeCsv(filePath, data);
    }
}
