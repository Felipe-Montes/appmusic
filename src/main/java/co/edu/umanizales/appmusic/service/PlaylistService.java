package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Playlist;
import co.edu.umanizales.appmusic.model.User;
import co.edu.umanizales.appmusic.model.Song;
import co.edu.umanizales.appmusic.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final CsvService csvService;
    private final UserService userService;
    private final SongService songService;
    @Value("${storage.playlists.path}")
    private String filePath;
    private final List<Playlist> playlists = new ArrayList<>();

    @PostConstruct
    void loadPlaylistsFromCsv() {
        List<String[]> rows = csvService.readCsv(filePath);
        playlists.clear();
        for (String[] row : rows) {
            if (row == null || row.length < 2) {
                continue;
            }
            String id = row[0] != null ? row[0].trim() : "";
            String name = row[1] != null ? row[1].trim() : "";
            String userId = row.length > 2 && row[2] != null ? row[2].trim() : "";
            String songIdsStr = row.length > 3 && row[3] != null ? row[3].trim() : "";
            if (id.isEmpty() || name.isEmpty()) {
                continue;
            }
            User user = !userId.isBlank() ? userService.getUserById(userId) : null;
            List<Song> songs = null;
            if (!songIdsStr.isBlank()) {
                songs = new ArrayList<>();
                String[] parts = songIdsStr.split(";");
                for (String sid : parts) {
                    if (sid == null || sid.isBlank()) continue;
                    Song s = songService.getSongById(sid.trim());
                    if (s != null) songs.add(s);
                }
            }
            playlists.add(new Playlist(id, name, user, songs));
        }
    }

    public List<Playlist> getAllPlaylists() {
        return playlists;
    }

    public Playlist getPlaylistById(String id) {
        for (Playlist playlist : playlists) {
            if (playlist.getIdPlaylist().equals(id)) {
                return playlist;
            }
        }
        return null;
    }

    public void addPlaylist(Playlist playlist) {
        // Validación de unicidad por id
        for (Playlist existing : playlists) {
            if (existing.getIdPlaylist().equals(playlist.getIdPlaylist())) {
                throw new DuplicateResourceException("Ya existe una playlist con el mismo id: " + playlist.getIdPlaylist());
            }
        }
        // Validación de unicidad por nombre
        for (Playlist existing : playlists) {
            if (existing.getName().equalsIgnoreCase(playlist.getName())) {
                throw new DuplicateResourceException("Ya existe una playlist con el mismo nombre: " + playlist.getName());
            }
        }
        playlists.add(playlist);
        savePlaylistsToCsv();
    }

    public void updatePlaylist(Playlist playlist) {
        for (Playlist existing : playlists) {
            if (existing.getIdPlaylist().equals(playlist.getIdPlaylist())) {
                existing.setName(playlist.getName());
                existing.setUser(playlist.getUser());
                existing.setSongs(playlist.getSongs());
                break;
            }
        }
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
            String userId = (p.getUser() != null && p.getUser().getIdUser() != null) ? p.getUser().getIdUser() : "";
            String songsJoined = "";
            if (p.getSongs() != null && !p.getSongs().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Song s : p.getSongs()) {
                    if (s != null && s.getId() != null) {
                        if (sb.length() > 0) sb.append(';');
                        sb.append(s.getId());
                    }
                }
                songsJoined = sb.toString();
            }
            data.add(new String[]{p.getIdPlaylist(), p.getName(), userId, songsJoined});
        }
        csvService.writeCsv(filePath, data);
    }
}
