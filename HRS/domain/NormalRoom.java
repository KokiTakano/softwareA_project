package HRS.domain;

// entity/NormalRoom.java (普通の部屋)
import java.math.BigDecimal;

public class NormalRoom extends Room {
    public NormalRoom(int roomNumber, BigDecimal pricePerNight) {
        super(roomNumber, pricePerNight, "普通の部屋");
    }
}