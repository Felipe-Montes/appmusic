package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CsvService csvService;
    private final String filePath = "data/users.csv";
    private final List<User> users = new ArrayList<>();

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
