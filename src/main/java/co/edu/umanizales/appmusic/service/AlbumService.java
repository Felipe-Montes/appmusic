package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Album;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final CsvService csvService;
    private final String filePath = "data/albums.csv";
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
                    a.getId(), a.getTitle(), String.valueOf(a.getDuration())
            });
        }
        csvService.writeCsv(filePath, data);
    }

}
