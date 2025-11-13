package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SongService {
    private final CsvService csvService;
    private final String filePath = "data/songs.csv";
    private final List<Song> songs = new ArrayList<>();

    public List<Song> getAllSongs() {
        return songs;
    }

    public Optional<Song> getSongById(String id) {
        for (Song song : songs) {
            if (song.getId().equals(id)) {
                return Optional.of(song);
            }
        }
        return Optional.empty();
    }

    public void addSong(Song song) {
        songs.add(song);
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
                    s.getId(), s.getTitle(), String.valueOf(s.getDuration()), s.getMusicGenre().toString()
            });
        }
        csvService.writeCsv(filePath, data);
    }
}
