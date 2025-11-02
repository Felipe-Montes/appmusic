package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor

public class Subscription {
    private String idSubscription;
    private SubscriptionType subscriptionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Payment> payments;

}
