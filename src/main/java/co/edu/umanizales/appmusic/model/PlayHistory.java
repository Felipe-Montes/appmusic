package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PlayHistory {
    private String idHistory;
    private User user;
    private Song song;
    private LocalDateTime playDate;

}
