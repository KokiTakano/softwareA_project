package HRS.dto;

import java.math.BigDecimal;

public class CheckInOutResponse {
    private boolean success;
    private String message;
    private BigDecimal amountDue;

    public CheckInOutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CheckInOutResponse(boolean success, String message, BigDecimal amountDue) {
        this.success = success;
        this.message = message;
        this.amountDue = amountDue;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }
}