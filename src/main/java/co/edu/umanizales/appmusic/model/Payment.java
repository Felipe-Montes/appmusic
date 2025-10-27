package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Payment {
    private String idPayment;
    private double amount;
    private LocalDate paymentDate;
    private User user;

    //constructor que inicializa todos los atributos
    public Payment(String idPayment, double amount, LocalDate paymentDate, User user) {
        this.idPayment = idPayment;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.user = user;
    }
}
