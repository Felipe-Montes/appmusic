package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor

public class Payment {
    private String idPayment;
    private double amount;
    private LocalDate paymentDate;
    private User user;

}
