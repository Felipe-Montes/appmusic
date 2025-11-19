package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.*;
import co.edu.umanizales.appmusic.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final CsvService csvService;
    private final ArtistService artistService;
    private final AlbumService albumService;
    @Value("${storage.songs.path}")
    private String filePath;
    private final List<Song> songs = new ArrayList<>();

    @PostConstruct
    void loadSongsFromCsv() {
        List<String[]> rows = csvService.readCsv(filePath);
        songs.clear();
        for (String[] row : rows) {
            if (row == null || row.length < 4) {
                continue;
            }
            String id = row[0] != null ? row[0].trim() : "";
            String title = row[1] != null ? row[1].trim() : "";
            String durationStr = row[2] != null ? row[2].trim() : "0";
            String genreStr = row[3] != null ? row[3].trim() : "";
            String artistId = row.length > 4 && row[4] != null ? row[4].trim() : "";
            String albumId = row.length > 5 && row[5] != null ? row[5].trim() : "";
            if (id.isEmpty() || title.isEmpty()) {
                continue;
            }
            double duration = 0;
            try { duration = Double.parseDouble(durationStr); } catch (NumberFormatException ignored) {}
            MusicGenre genre;
            try { genre = MusicGenre.valueOf(genreStr); } catch (Exception e) { continue; }
            Artist artist = !artistId.isBlank() ? artistService.getArtistById(artistId) : null;
            Album album = !albumId.isBlank() ? albumService.getAlbumById(albumId) : null;
            songs.add(new Song(id, title, duration, genre, artist, album));
        }
    }

    public List<Song> getAllSongs() {
        return songs;
    }

    public Song getSongById(String id) {
        for (Song song : songs) {
            if (song.getId().equals(id)) {
                return song;
            }
        }
        return null;
    }

    public void addSong(Song song) {
        // Validación de unicidad por id
        for (Song existing : songs) {
            if (existing.getId().equals(song.getId())) {
                throw new DuplicateResourceException("Ya existe una canción con el mismo id: " + song.getId());
            }
        }
        songs.add(song);
        saveSongsToCsv();
    }

    public void updateSong(Song song) {
        for (Song existing : songs) {
            if (existing.getId().equals(song.getId())) {
                existing.setTitle(song.getTitle());
                existing.setDuration(song.getDuration());
                existing.setMusicGenre(song.getMusicGenre());
                break;
            }
        }
        saveSongsToCsv();
    }

    public void deleteSong(String id) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId().equals(id)) {
                songs.remove(i);
                break;
            }
        }
        saveSongsToCsv();
    }

    private void saveSongsToCsv() {
        List<String[]> data = new ArrayList<>();
        for (Song s : songs) {
            data.add(new String[]{
                    s.getId(),
                    s.getTitle(),
                    String.valueOf(s.getDuration()),
                    s.getMusicGenre().toString(),
                    (s.getArtist() != null && s.getArtist().getIdArtist() != null) ? s.getArtist().getIdArtist() : "",
                    (s.getAlbum() != null && s.getAlbum().getId() != null) ? s.getAlbum().getId() : ""
            });
        }
        csvService.writeCsv(filePath, data);
    }
}
