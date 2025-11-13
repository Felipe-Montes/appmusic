package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final CsvService csvService;
    private final String filePath = "data/playlists.csv";
    private final List<Playlist> playlists = new ArrayList<>();

    public List<Playlist> getAllPlaylists() {
        return playlists;
    }

    public Optional<Playlist> getPlaylistById(String id) {
        for (Playlist playlist : playlists) {
            if (playlist.getIdPlaylist().equals(id)) {
                return Optional.of(playlist);
            }
        }
        return Optional.empty();
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        savePlaylistsToCsv();
    }

    public void deletePlaylist(String id) {
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getIdPlaylist().equals(id)) {
                playlists.remove(i);
                break;
            }
        }
        savePlaylistsToCsv();
    }

    private void savePlaylistsToCsv() {
        List<String[]> data = new ArrayList<>();
        for (Playlist p : playlists) {
            data.add(new String[]{p.getIdPlaylist(), p.getName()});
        }
        csvService.writeCsv(filePath, data);
    }
}
