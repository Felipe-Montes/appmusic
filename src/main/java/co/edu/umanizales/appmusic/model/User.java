package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class User {
    private String idUser;
    private String name;
    private String email;
    private SubscriptionType subscriptionType;
    private List<Playlist> playlists;

    public void showInformation() {
        System.out.println("User: " + name + " |Plan: " + subscriptionType);
    }
}
