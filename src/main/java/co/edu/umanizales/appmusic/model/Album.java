package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter

public class Album extends MultimediaContent {
    private Artist artist;
    private List<Song> songs;

    //constructor que inicializa todos los atributos
    public Album(String id, String title, double duration, Artist artist, List<Song> songs) {
        super(id, title, duration);
        this.artist = artist;
        this.songs = songs;
    }

    // Sobrescribe el metodo play() para reproducir el álbum completo
    @Override
    public void play() {
        System.out.println("Reproduciendo el álbum: " + this.getTitle() + " de " + artist.getName());
    }
}
