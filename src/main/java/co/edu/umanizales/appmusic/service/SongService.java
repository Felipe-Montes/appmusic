package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final CsvService csvService;
    @Value("${storage.songs.path}")
    private String filePath;
    private final List<Song> songs = new ArrayList<>();

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
                    s.getId(), s.getTitle(), String.valueOf(s.getDuration()), s.getMusicGenre().toString()
            });
        }
        csvService.writeCsv(filePath, data);
    }
}
