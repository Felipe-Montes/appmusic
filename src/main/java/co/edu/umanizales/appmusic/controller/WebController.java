package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.*;
import co.edu.umanizales.appmusic.service.AlbumService;
import co.edu.umanizales.appmusic.service.ArtistService;
import co.edu.umanizales.appmusic.service.SongService;
import co.edu.umanizales.appmusic.service.UserService;
import co.edu.umanizales.appmusic.service.PlaylistService;
import co.edu.umanizales.appmusic.service.PaymentService;
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
import java.time.LocalDate;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class WebController {
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;
    private final UserService userService;
    private final PlaylistService playlistService;
    private final PaymentService paymentService;

    /**
     * Página de inicio del GUI.
     * Agrega contadores de artistas, álbumes y canciones para mostrar.
     * @param model modelo de la vista
     * @return nombre de la plantilla Thymeleaf "index"
     */
    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("artistsCount", artistService.getAllArtists().size());
        model.addAttribute("albumsCount", albumService.getAllAlbums().size());
        model.addAttribute("songsCount", songService.getAllSongs().size());
        return "index";
    }

    // ARTISTS
    /**
     * Vista de listado y creación de artistas.
     * @param model modelo con la lista de artistas
     * @return plantilla "artists"
     */
    @GetMapping("/artists")
    public String artists(Model model) {
        model.addAttribute("artists", artistService.getAllArtists());
        return "artists";
    }

    /**
     * Crea un artista a partir de los parámetros del formulario.
     * En caso de duplicados, redirige con mensaje de error.
     */
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
    /**
     * Vista de listado y creación de álbumes. Incluye artistas para el selector.
     * @param model modelo con álbumes y artistas
     * @return plantilla "albums"
     */
    @GetMapping("/albums")
    public String albums(Model model) {
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("artists", artistService.getAllArtists());
        return "albums";
    }

    /**
     * Crea un álbum asociado opcionalmente a un artista.
     * Maneja conflicto por id/título duplicado mediante redirección con error.
     */
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
    /**
     * Vista de listado y creación de canciones. Incluye artistas, álbumes y géneros.
     */
    @GetMapping("/songs")
    public String songs(Model model) {
        model.addAttribute("songs", songService.getAllSongs());
        model.addAttribute("artists", artistService.getAllArtists());
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("genres", MusicGenre.values());
        return "songs";
    }

    /**
     * Crea una canción con género y relaciones opcionales a artista y álbum.
     * Valida unicidad por id y redirige con mensaje en caso de duplicado.
     */
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
    /**
     * Vista de usuarios con listado y creación.
     */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("subscriptionTypes", SubscriptionType.values());
        return "users";
    }

    /**
     * Crea un usuario validando unicidad de id y email.
     */
    @PostMapping("/users")
    public String createUser(
            @RequestParam String idUser,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subscriptionType,
            @RequestParam(required = false) String idPayment,
            @RequestParam(required = false) String paymentDate
    ) {
        SubscriptionType st = SubscriptionType.valueOf(subscriptionType);
        try {
            userService.addUser(new User(idUser, name, email, st, null));
            // Si es un plan pago, exigir y registrar el pago
            if (st == SubscriptionType.PREMIUM || st == SubscriptionType.FAMILY) {
                if (idPayment == null || idPayment.isBlank() || paymentDate == null || paymentDate.isBlank()) {
                    String msg = URLEncoder.encode("Se requieren idPayment y paymentDate para planes PREMIUM o FAMILY", StandardCharsets.UTF_8);
                    return "redirect:/ui/users?error=" + msg;
                }
                LocalDate date;
                try { date = LocalDate.parse(paymentDate); } catch (Exception e) {
                    String msg = URLEncoder.encode("Fecha de pago inválida (use AAAA-MM-DD)", StandardCharsets.UTF_8);
                    return "redirect:/ui/users?error=" + msg;
                }
                double defAmount = (st == SubscriptionType.PREMIUM) ? 3_000_000D : 6_000_000D;
                paymentService.addPayment(new Payment(idPayment, defAmount, date, userService.getUserById(idUser)));
            }
            return "redirect:/ui/users";
        } catch (DuplicateResourceException ex) {
            String msg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/ui/users?error=" + msg;
        }
    }

    // PLAYLISTS
    /**
     * Vista de playlists con listado y formularios de crear/actualizar.
     * Carga usuarios y canciones para selección.
     */
    @GetMapping("/playlists")
    public String playlists(Model model) {
        model.addAttribute("playlists", playlistService.getAllPlaylists());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("songs", songService.getAllSongs());
        return "playlists";
    }

    @GetMapping("/payments")
    public String payments(Model model) {
        var data = paymentService.getAllPayments().stream()
                .filter(p -> p.getPaymentDate() != null && p.getUser() != null && p.getUser().getSubscriptionType() != null)
                .filter(p -> p.getUser().getSubscriptionType() == SubscriptionType.PREMIUM || p.getUser().getSubscriptionType() == SubscriptionType.FAMILY)
                .collect(Collectors.groupingBy(Payment::getPaymentDate,
                        Collectors.groupingBy(p -> p.getUser().getSubscriptionType(), Collectors.summingDouble(Payment::getAmount))
                ));
        record Row(LocalDate fecha, long family, long premium, long total) {}
        List<Row> rows = data.entrySet().stream()
                .map(e -> {
                    double fam = e.getValue().getOrDefault(SubscriptionType.FAMILY, 0.0);
                    double pre = e.getValue().getOrDefault(SubscriptionType.PREMIUM, 0.0);
                    long lf = (long) fam;
                    long lp = (long) pre;
                    return new Row(e.getKey(), lf, lp, lf + lp);
                })
                .sorted(Comparator.comparing(Row::fecha))
                .toList();
        model.addAttribute("rows", rows);
        return "payments";
    }

    /**
     * Crea una playlist con usuario opcional y múltiples canciones.
     * Persiste y maneja duplicados por id/nombre.
     */
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

    /**
     * Actualiza una playlist existente (nombre, usuario y canciones).
     */
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

    /**
     * Elimina una playlist por su identificador.
     */
    @PostMapping("/playlists/delete")
    public String deletePlaylist(@RequestParam String idPlaylist) {
        playlistService.deletePlaylist(idPlaylist);
        return "redirect:/ui/playlists";
    }
}
