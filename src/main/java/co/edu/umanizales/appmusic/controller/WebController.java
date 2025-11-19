package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.*;
import co.edu.umanizales.appmusic.service.AlbumService;
import co.edu.umanizales.appmusic.service.ArtistService;
import co.edu.umanizales.appmusic.service.SongService;
import co.edu.umanizales.appmusic.service.UserService;
import co.edu.umanizales.appmusic.service.PlaylistService;
import co.edu.umanizales.appmusic.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class WebController {
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;
    private final UserService userService;
    private final PlaylistService playlistService;

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("artistsCount", artistService.getAllArtists().size());
        model.addAttribute("albumsCount", albumService.getAllAlbums().size());
        model.addAttribute("songsCount", songService.getAllSongs().size());
        return "index";
    }

    // ARTISTS
    @GetMapping("/artists")
    public String artists(Model model) {
        model.addAttribute("artists", artistService.getAllArtists());
        return "artists";
    }

    @PostMapping("/artists")
    public String createArtist(
            @RequestParam String idArtist,
            @RequestParam String name
    ) {
        try {
            artistService.addArtist(new Artist(idArtist, name, null, null));
            return "redirect:/ui/artists";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/artists?error=" + msg;
        }
    }

    // ALBUMS
    @GetMapping("/albums")
    public String albums(Model model) {
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("artists", artistService.getAllArtists());
        return "albums";
    }

    @PostMapping("/albums")
    public String createAlbum(
            @RequestParam String id,
            @RequestParam String title,
            @RequestParam double duration,
            @RequestParam String artistId
    ) {
        Artist artist = artistService.getArtistById(artistId);
        try {
            albumService.addAlbum(new Album(id, title, duration, artist, null));
            return "redirect:/ui/albums";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/albums?error=" + msg;
        }
    }

    // SONGS
    @GetMapping("/songs")
    public String songs(Model model) {
        model.addAttribute("songs", songService.getAllSongs());
        model.addAttribute("artists", artistService.getAllArtists());
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("genres", MusicGenre.values());
        return "songs";
    }

    @PostMapping("/songs")
    public String createSong(
            @RequestParam String id,
            @RequestParam String title,
            @RequestParam double duration,
            @RequestParam String genre,
            @RequestParam(required = false) String artistId,
            @RequestParam(required = false) String albumId
    ) {
        Artist artist = artistId != null && !artistId.isBlank() ? artistService.getArtistById(artistId) : null;
        Album album = albumId != null && !albumId.isBlank() ? albumService.getAlbumById(albumId) : null;
        MusicGenre g = MusicGenre.valueOf(genre);
        try {
            songService.addSong(new Song(id, title, duration, g, artist, album));
            return "redirect:/ui/songs";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/songs?error=" + msg;
        }
    }

    // USERS
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("subscriptionTypes", SubscriptionType.values());
        return "users";
    }

    @PostMapping("/users")
    public String createUser(
            @RequestParam String idUser,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subscriptionType
    ) {
        SubscriptionType st = SubscriptionType.valueOf(subscriptionType);
        try {
            userService.addUser(new User(idUser, name, email, st, null));
            return "redirect:/ui/users";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/users?error=" + msg;
        }
    }

    // PLAYLISTS
    @GetMapping("/playlists")
    public String playlists(Model model) {
        model.addAttribute("playlists", playlistService.getAllPlaylists());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("songs", songService.getAllSongs());
        return "playlists";
    }

    @PostMapping("/playlists")
    public String createPlaylist(
            @RequestParam String idPlaylist,
            @RequestParam String name,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false, name = "songIds") List<String> songIds
    ) {
        User user = (userId != null && !userId.isBlank()) ? userService.getUserById(userId) : null;
        List<Song> selectedSongs = null;
        if (songIds != null && !songIds.isEmpty()) {
            selectedSongs = new java.util.ArrayList<>();
            for (String sid : songIds) {
                Song s = songService.getSongById(sid);
                if (s != null) selectedSongs.add(s);
            }
        }
        try {
            playlistService.addPlaylist(new Playlist(idPlaylist, name, user, selectedSongs));
            return "redirect:/ui/playlists";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/playlists?error=" + msg;
        }
    }

    @PostMapping("/playlists/update")
    public String updatePlaylist(
            @RequestParam String idPlaylist,
            @RequestParam String name,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false, name = "songIds") List<String> songIds
    ) {
        User user = (userId != null && !userId.isBlank()) ? userService.getUserById(userId) : null;
        List<Song> selectedSongs = null;
        if (songIds != null && !songIds.isEmpty()) {
            selectedSongs = new java.util.ArrayList<>();
            for (String sid : songIds) {
                Song s = songService.getSongById(sid);
                if (s != null) selectedSongs.add(s);
            }
        }
        try {
            playlistService.updatePlaylist(new Playlist(idPlaylist, name, user, selectedSongs));
            return "redirect:/ui/playlists";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/playlists?error=" + msg;
        }
    }

    @PostMapping("/playlists/delete")
    public String deletePlaylist(@RequestParam String idPlaylist) {
        playlistService.deletePlaylist(idPlaylist);
        return "redirect:/ui/playlists";
    }
}
