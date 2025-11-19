package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.User;
import co.edu.umanizales.appmusic.model.SubscriptionType;
import co.edu.umanizales.appmusic.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CsvService csvService;
    @Value("${storage.users.path}")
    private String filePath;
    private final List<User> users = new ArrayList<>();

    @PostConstruct
    void loadUsersFromCsv() {
        List<String[]> rows = csvService.readCsv(filePath);
        users.clear();
        for (String[] row : rows) {
            if (row == null || row.length < 4) {
                continue;
            }
            String id = row[0] != null ? row[0].trim() : "";
            String name = row[1] != null ? row[1].trim() : "";
            String email = row[2] != null ? row[2].trim() : "";
            String sub = row[3] != null ? row[3].trim() : "FREE";
            if (id.isEmpty() || name.isEmpty()) {
                continue;
            }
            SubscriptionType st;
            try { st = SubscriptionType.valueOf(sub); } catch (Exception e) { st = SubscriptionType.FREE; }
            users.add(new User(id, name, email, st, null));
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(String id) {
        for (User user : users) {
            if (user.getIdUser().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        // Validación de unicidad por id
        for (User existing : users) {
            if (existing.getIdUser().equals(user.getIdUser())) {
                throw new DuplicateResourceException("Ya existe un usuario con el mismo id: " + user.getIdUser());
            }
        }
        // Validación opcional por email (si no está vacío)
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            for (User existing : users) {
                if (user.getEmail().equalsIgnoreCase(existing.getEmail())) {
                    throw new DuplicateResourceException("Ya existe un usuario con el mismo email: " + user.getEmail());
                }
            }
        }
        users.add(user);
        saveUsersToCsv();
    }

    public void updateUser(User user) {
        for (User existing : users) {
            if (existing.getIdUser().equals(user.getIdUser())) {
                existing.setName(user.getName());
                existing.setEmail(user.getEmail());
                existing.setSubscriptionType(user.getSubscriptionType());
                break;
            }
        }
        saveUsersToCsv();
    }

    public void deleteUser(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getIdUser().equals(id)) {
                users.remove(i);
                break;
            }
        }
        saveUsersToCsv();
    }

    private void saveUsersToCsv() {
        List<String[]> data = new ArrayList<>();
        for (User u : users) {
            data.add(new String[]{
                    u.getIdUser(), u.getName(), u.getEmail(), u.getSubscriptionType().toString()
            });
        }
        csvService.writeCsv(filePath, data);
    }
}
