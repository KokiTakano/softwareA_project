// entity/Room.java (部屋エンティティ)
package HRS.domain;

import java.math.BigDecimal; // 金額はBigDecimalを使うのが安全

public class Room {
    private int roomNumber; // 部屋番号 (intよりStringの方が柔軟)
    private BigDecimal pricePerNight; // 1泊あたりの値段
    private RoomStatus status; // 状態 (enumで定義)
    private String roomType; // 部屋の種類 (例: "普通の部屋", "スイートルーム")

    public enum RoomStatus {
        VACANT, // 空室
        OCCUPIED, // 使用中
    }

    public Room(int roomNumber, BigDecimal pricePerNight, String roomType) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.roomType = roomType;
        this.status = RoomStatus.VACANT; // 初期状態は空室
    }

    // Getter methods
    public int getRoomNumber() {
        return roomNumber;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public String getRoomType() {
        return roomType;
    }

    // Setter methods
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    // 部屋情報を取得する操作
    public String getRoomInfo() {
        return "部屋番号: " + roomNumber + ", 種類: " + roomType + ", 料金: " + pricePerNight + ", 状態: " + status;
    }

    // 状態を空室にする操作 (クラス図より)
    public void setStatusToVacant() {
        this.status = RoomStatus.VACANT;
    }
    // 状態を使用中にする操作 (クラス図より)
    public void setStatusToOccupied() {
        this.status = RoomStatus.OCCUPIED;
    }
}

