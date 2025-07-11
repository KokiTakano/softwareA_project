package HRS.dto;

import java.math.BigDecimal;

public class ReservationResponse {
    private String reservationId;
    private BigDecimal totalAmount;
    private boolean success;
    private String message;

    public ReservationResponse(String reservationId, BigDecimal totalAmount, boolean success, String message) {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.success = success;
        this.message = message;
    }

    public String getReservationId() {
        return reservationId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}