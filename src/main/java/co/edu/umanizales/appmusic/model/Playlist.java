package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class Playlist {
    private String idPlaylist;
    private String name;
    private User user;
    private List<Song> songs;

}
