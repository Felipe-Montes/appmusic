package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Song extends MultimediaContent {
    private Genre genre;
    private Artist artist;
    private Album album;

    //constructor que inicializa todos los atributos
    public Song(String id, String title, double duration, Genre genre, Artist artist, Album album) {
        super(id, title, duration);
        this.genre = genre;
        this.artist = artist;
        this.album = album;
    }

    //Implementación del metodo abstracto de la clase padre
    @Override
    public void play() {
        System.out.println("Reproduciendo la canción: " + title + " de " + artist.getName());
    }
}
