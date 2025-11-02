package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class PlayHistory {
    private String idHistory;
    private User user;
    private Song song;
    private LocalDateTime playDate;

}
