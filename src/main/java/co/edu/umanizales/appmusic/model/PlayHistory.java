package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PlayHistory {
    private String idHistory;
    private User user;
    private Song song;
    private LocalDateTime playDate;

    //constructor que inicializa todos los atributos
    public PlayHistory(String idHistory, User user, Song song, LocalDateTime playDate) {
        this.idHistory = idHistory;
        this.user = user;
        this.song = song;
        this.playDate = playDate;
    }
}
