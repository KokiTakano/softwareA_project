package HRS.domain;

// entity/Reservation.java (予約エンティティ)
import java.time.LocalDate; // 日付型
import java.math.BigDecimal; // 金額型

public class Reservation {
    private String reservationId; // 予約番号 (string)
    private LocalDate checkInDate; // チェックイン日 (LocalDate)
    private int stayDurationDays; // 宿泊日数 (int)
    private ReservationStatus status; // 予約状態 (enum)
    private Room reservedRoom; // 予約された部屋 (Roomオブジェクトへの参照)
    private BigDecimal totalAmount; // 総額

    public enum ReservationStatus {
        PENDING,    // 保留中
        CONFIRMED,  // 予約確定済み
        CHECKED_IN, // チェックイン済み
        CHECKED_OUT,// チェックアウト済み
    }

    public Reservation(String reservationId, Room reservedRoom, LocalDate checkInDate, int stayDurationDays, BigDecimal totalAmount) {
        this.reservationId = reservationId;
        this.reservedRoom = reservedRoom;
        this.checkInDate = checkInDate;
        this.stayDurationDays = stayDurationDays;
        this.totalAmount = totalAmount;
        this.status = ReservationStatus.PENDING; // 初期状態は保留
    }

    // Getter methods
    public String getReservationId() {
        return reservationId;
    }

    public Room getReservedRoom() {
        return reservedRoom;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public int getStayDurationDays() {
        return stayDurationDays;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    // Setter methods
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    // クラス図の操作を実装
    public String getReservationInfo() {
        return "予約番号: " + reservationId + ", 部屋: " + reservedRoom.getRoomNumber() + ", 日付: " + checkInDate + ", 泊数: " + stayDurationDays;
    }

    public void setCheckedIn() {
        this.status = ReservationStatus.CHECKED_IN;
        this.reservedRoom.setStatusToOccupied(); // 部屋の状態も使用中に
    }

    public void setCheckedOut() {
        this.status = ReservationStatus.CHECKED_OUT;
        // 部屋の状態は清掃中などに更新される想定だが、ここではシンプルに空室にするか、別の処理に任せる
        // this.reservedRoom.setStatusToVacant(); // チェックアウト後の清掃は別プロセスと仮定
    }
}
