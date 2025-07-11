package HRS.domain;

// entity/Payment.java (支払いエンティティ)
import java.math.BigDecimal;

public class Payment {
    private String paymentId;
    private String reservationId; // どの予約に対する支払いか
    private BigDecimal amount;
    private PaymentStatus status; // 支払い状態 (enum)

    public enum PaymentStatus {
        UNPAID, // 未払い
        PAID,   // 支払い済み
    }

    public Payment(String paymentId, String reservationId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = PaymentStatus.UNPAID; // 初期状態は未払い
    }

    // Getter methods
    public String getPaymentId() {
        return paymentId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    // Setter methods
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    // クラス図の操作を実装
    public void setPaymentCompleted() {
        this.status = PaymentStatus.PAID;
    }
}