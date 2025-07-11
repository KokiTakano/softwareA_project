package HRS.dto;

import java.time.LocalDate;

public class RoomSearchRequest {
    private LocalDate checkInDate;
    private int stayDurationDays;
    private String desiredRoomType; // 希望する部屋の種類

    public RoomSearchRequest(LocalDate checkInDate, int stayDurationDays, String desiredRoomType) {
        this.checkInDate = checkInDate;
        this.stayDurationDays = stayDurationDays;
        this.desiredRoomType = desiredRoomType;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public int getStayDurationDays() {
        return stayDurationDays;
    }

    public String getDesiredRoomType() {
        return desiredRoomType;
    }
}