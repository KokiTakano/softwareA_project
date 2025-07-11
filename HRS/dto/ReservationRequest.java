package HRS.dto;

import java.time.LocalDate;

public class ReservationRequest {
    private int roomNumber;
    private LocalDate checkInDate;
    private int stayDurationDays;

    public ReservationRequest(int roomNumber, LocalDate checkInDate, int stayDurationDays) {
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.stayDurationDays = stayDurationDays;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public int getStayDurationDays() {
        return stayDurationDays;
    }
}