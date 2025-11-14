package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Album;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final CsvService csvService;
    @Value("${storage.albums.path}")
    private String filePath;
    private final List<Album> albums = new ArrayList<>();

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
