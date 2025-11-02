package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class Artist {
    private String idArtist;
    private String name;
    private List<Song> songs;
    private List<Album> albums;

}
