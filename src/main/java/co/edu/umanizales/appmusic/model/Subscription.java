package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Subscription {
    private String idSubscription;
    private SubscriptionType subscriptionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Payment> payments;

    //constructor que inicializa todos los atributos
    public Subscription(String idSubscription, SubscriptionType subscriptionType, LocalDate startDate, LocalDate endDate, List<Payment> payments) {
        this.idSubscription = idSubscription;
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payments = payments;
    }
}
