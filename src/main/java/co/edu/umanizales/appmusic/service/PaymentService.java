package co.edu.umanizales.appmusic.service;

import co.edu.umanizales.appmusic.model.Payment;
import co.edu.umanizales.appmusic.model.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CsvService csvService;
    private final UserService userService;
    @Value("${storage.payments.path}")
    private String filePath;
    private final List<Payment> payments = new ArrayList<>();

    @PostConstruct
    void loadPaymentsFromCsv() {
        List<String[]> rows = csvService.readCsv(filePath);
        payments.clear();
        for (String[] row : rows) {
            if (row == null || row.length < 4) {
                continue;
            }
            String id = row[0] != null ? row[0].trim() : "";
            String amountStr = row[1] != null ? row[1].trim() : "0";
            String dateStr = row[2] != null ? row[2].trim() : null;
            String userId = row[3] != null ? row[3].trim() : "";
            if (id.isEmpty()) continue;
            double amount = 0;
            try { amount = Double.parseDouble(amountStr); } catch (NumberFormatException ignored) {}
            LocalDate date = null;
            try { if (dateStr != null && !dateStr.isBlank()) date = LocalDate.parse(dateStr); } catch (Exception ignored) {}
            User user = !userId.isBlank() ? userService.getUserById(userId) : null;
            payments.add(new Payment(id, amount, date, user));
        }
    }

    public List<Payment> getAllPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        // Reemplazar si existe mismo id
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getIdPayment().equals(payment.getIdPayment())) {
                payments.set(i, payment);
                savePaymentsToCsv();
                return;
            }
        }
        payments.add(payment);
        savePaymentsToCsv();
    }

    private void savePaymentsToCsv() {
        List<String[]> data = new ArrayList<>();
        for (Payment p : payments) {
            data.add(new String[]{
                    p.getIdPayment(),
                    String.valueOf(p.getAmount()),
                    p.getPaymentDate() != null ? p.getPaymentDate().toString() : "",
                    (p.getUser() != null && p.getUser().getIdUser() != null) ? p.getUser().getIdUser() : ""
            });
        }
        csvService.writeCsv(filePath, data);
    }
}
