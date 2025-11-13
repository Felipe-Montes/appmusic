package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.PlayHistory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayHistoryService {
    private final List<PlayHistory> histories = new ArrayList<>();

    public List<PlayHistory> findAll() {
        return histories;
    }

    public PlayHistory findById(String id) {
        for (PlayHistory history : histories) {
            if (history.getIdHistory().equals(id)) {
                return history;
            }
        }
        return null;
    }

    public void save(PlayHistory history) {
        boolean found = false;
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).getIdHistory().equals(history.getIdHistory())) {
                histories.set(i, history);
                found = true;
                break;
            }
        }
        if (!found) {
            histories.add(history);
        }
    }

    public void delete(String id) {
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).getIdHistory().equals(id)) {
                histories.remove(i);
                break;
            }
        }
    }
}
