package co.edu.umanizales.appmusic.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
    public List<String[]> readCsv(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void writeCsv(String filePath, List<String[]> data) {
        try {
            File target = new File(filePath);
            File parent = target.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(target))) {
                for (String[] line : data) {
                    bw.write(String.join(",", line));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateCsvRow(String filePath, int keyColumnIndex, String keyValue, String[] newRow) {
        List<String[]> rows = readCsv(filePath);
        boolean updated = false;
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (keyColumnIndex >= 0 && keyColumnIndex < row.length && row[keyColumnIndex].equals(keyValue)) {
                rows.set(i, newRow);
                updated = true;
                break;
            }
        }
        if (updated) {
            writeCsv(filePath, rows);
        }
        return updated;
    }
}
