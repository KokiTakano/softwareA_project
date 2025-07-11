package HRS.dto;

public class RoomTypeResult {
    private String roomType;
    private long availableCount; // その種類の利用可能な部屋数
    private int sampleRoomNumber; // 例として利用可能な部屋の番号

    public RoomTypeResult(String roomType, long availableCount, int sampleRoomNumber) {
        this.roomType = roomType;
        this.availableCount = availableCount;
        this.sampleRoomNumber = sampleRoomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public long getAvailableCount() {
        return availableCount;
    }

    public int getSampleRoomNumber() {
        return sampleRoomNumber;
    }
}