package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Playlist {
    private String idPlaylist;
    private String name;
    private User user;
    private List<Song> songs;

    //constructor que inicializa todos los atributos
    public Playlist(String idPlaylist, String name, User user, List<Song> songs) {
        this.idPlaylist = idPlaylist;
        this.name = name;
        this.user = user;
        this.songs = songs;
    }
}
