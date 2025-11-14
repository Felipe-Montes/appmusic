package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final CsvService csvService;
    @Value("${storage.artists.path}")
    private String filePath;
    private final List<Artist> artists = new ArrayList<>();

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
