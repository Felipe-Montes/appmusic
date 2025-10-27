package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Artist {
    private String idArtist;
    private String name;
    private List<Song> songs;
    private List<Album> albums;

    //constructor que inicializa todos los atributos
    public Artist(String idArtist, String name, List<Song> songs, List<Album> albums) {
        this.idArtist = idArtist;
        this.name = name;
        this.songs = songs;
        this.albums = albums;
    }
}
