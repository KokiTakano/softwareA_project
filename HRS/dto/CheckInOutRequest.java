package HRS.dto;

public class CheckInOutRequest {
    private String reservationId;
    private int roomNumber;

    public CheckInOutRequest(String reservationId) {
        this.reservationId = reservationId;
    }
    public CheckInOutRequest(String reservationId, int roomNumber) {
        this.reservationId = reservationId;
        this.roomNumber = roomNumber;
    }

    public String getReservationId() {
        return reservationId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
