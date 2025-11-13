package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final CsvService csvService;
    private final String filePath = "data/artists.csv";
    private final List<Artist> artists = new ArrayList<>();

    public List<Artist> getAllArtists() {
        return artists;
    }

    public Optional<Artist> getArtistById(String id) {
        for (Artist artist : artists) {
            if (artist.getIdArtist().equals(id)) {
                return Optional.of(artist);
            }
        }
        return Optional.empty();
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
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
