package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.MultimediaContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MultimediaContentService {
    private final List<MultimediaContent> contents = new ArrayList<>();

    public List<MultimediaContent> findAll() {
        return contents;
    }

    public MultimediaContent findById(String id) {
        for (MultimediaContent content : contents) {
            if (content.getId().equals(id)) {
                return content;
            }
        }
        return null;
    }

    public void save(MultimediaContent content) {
        boolean found = false;
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getId().equals(content.getId())) {
                contents.set(i, content);
                found = true;
                break;
            }
        }
        if (!found) {
            contents.add(content);
        }
    }

    public void delete(String id) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getId().equals(id)) {
                contents.remove(i);
                break;
            }
        }
    }
}
