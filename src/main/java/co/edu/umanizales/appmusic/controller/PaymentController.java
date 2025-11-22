package co.edu.umanizales.appmusic.controller;

import co.edu.umanizales.appmusic.model.Payment;
import co.edu.umanizales.appmusic.model.SubscriptionType;
import co.edu.umanizales.appmusic.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/report")
    public ResponseEntity<?> getReport(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Payment> all = paymentService.getAllPayments();
        List<Payment> filtered = all.stream()
                .filter(p -> p.getPaymentDate() != null && p.getUser() != null && p.getUser().getSubscriptionType() != null)
                .filter(p -> p.getUser().getSubscriptionType() == SubscriptionType.PREMIUM || p.getUser().getSubscriptionType() == SubscriptionType.FAMILY)
                .filter(p -> from == null || !p.getPaymentDate().isBefore(from))
                .filter(p -> to == null || !p.getPaymentDate().isAfter(to))
                .toList();

        Map<LocalDate, Map<SubscriptionType, Double>> grouped = filtered.stream()
                .collect(Collectors.groupingBy(
                        Payment::getPaymentDate,
                        Collectors.groupingBy(p -> p.getUser().getSubscriptionType(), Collectors.summingDouble(Payment::getAmount))
                ));

        List<DayReportDTO> days = grouped.entrySet().stream()
                .map(e -> toDayReport(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(DayReportDTO::getFecha))
                .toList();

        if (from != null || to != null) {
            RangeReportDTO range = new RangeReportDTO(from, to, days);
            return ResponseEntity.ok(range);
        }
        return ResponseEntity.ok(days);
    }

    private static DayReportDTO toDayReport(LocalDate date, Map<SubscriptionType, Double> map) {
        double family = map.getOrDefault(SubscriptionType.FAMILY, 0.0);
        double premium = map.getOrDefault(SubscriptionType.PREMIUM, 0.0);
        List<SubscriptionValueDTO> subs = new ArrayList<>();
        subs.add(new SubscriptionValueDTO("FAMILY", (long) family));
        subs.add(new SubscriptionValueDTO("PREMIUM", (long) premium));
        long total = (long) (family + premium);
        return new DayReportDTO(date, subs, total);
    }

    public static class SubscriptionValueDTO {
        private String tipo_sub;
        private long valor;

        public SubscriptionValueDTO(String tipo_sub, long valor) {
            this.tipo_sub = tipo_sub;
            this.valor = valor;
        }

        public String getTipo_sub() { return tipo_sub; }
        public long getValor() { return valor; }
    }

    public static class DayReportDTO {
        private LocalDate fecha;
        private List<SubscriptionValueDTO> subscripciones;
        private long total_tipo;

        public DayReportDTO(LocalDate fecha, List<SubscriptionValueDTO> subscripciones, long total_tipo) {
            this.fecha = fecha;
            this.subscripciones = Objects.requireNonNullElseGet(subscripciones, ArrayList::new);
            this.total_tipo = total_tipo;
        }

        public LocalDate getFecha() { return fecha; }
        public List<SubscriptionValueDTO> getSubscripciones() { return subscripciones; }
        public long getTotal_tipo() { return total_tipo; }
    }

    public static class RangeReportDTO {
        private LocalDate fecha_inicial;
        private LocalDate fecha_final;
        private List<DayReportDTO> dias;

        public RangeReportDTO(LocalDate fecha_inicial, LocalDate fecha_final, List<DayReportDTO> dias) {
            this.fecha_inicial = fecha_inicial;
            this.fecha_final = fecha_final;
            this.dias = Objects.requireNonNullElseGet(dias, ArrayList::new);
        }

        public LocalDate getFecha_inicial() { return fecha_inicial; }
        public LocalDate getFecha_final() { return fecha_final; }
        public List<DayReportDTO> getDias() { return dias; }
    }
}
