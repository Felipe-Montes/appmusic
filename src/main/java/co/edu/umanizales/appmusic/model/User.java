package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class User {
    private String idUser;
    private String name;
    private String email;
    private Subscription subscription;
    private List<Playlist> playlists;

    //constructor que inicializa todos los atributos
    public User(String idUser, String name, String email, Subscription subscription, List<Playlist> playlists) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.subscription = subscription;
        this.playlists = playlists;
    }
}
