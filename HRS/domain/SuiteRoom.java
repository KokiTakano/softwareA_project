package HRS.domain;

// entity/SuiteRoom.java (スイートルーム)
import java.math.BigDecimal;

public class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber, BigDecimal pricePerNight) {
        super(roomNumber, pricePerNight, "スイートルーム");
    }
}