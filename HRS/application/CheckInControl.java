package HRS.application;

import HRS.data.HotelDataStore;
import HRS.dto.CheckInOutRequest;
import HRS.dto.CheckInOutResponse;
import HRS.domain.Reservation;
import HRS.domain.Room;

import java.time.LocalDate;
import java.util.Optional;

public class CheckInControl {
    private final HotelDataStore dataStore;

    public CheckInControl() {
        this.dataStore = HotelDataStore.getInstance();
    }

    public CheckInOutResponse processCheckIn(CheckInOutRequest request) {
        Optional<Reservation> reservationOpt = dataStore.findReservationById(request.getReservationId());

        if (!reservationOpt.isPresent()) {
            return new CheckInOutResponse(false, "予約番号が見つかりません。");
        }

        Reservation reservation = reservationOpt.get();

        if (!reservation.getCheckInDate().isEqual(LocalDate.now())) {
            return new CheckInOutResponse(false, "チェックイン日が本日ではありません。");
        }

        reservation.setCheckedIn();
        dataStore.saveReservation(reservation);

        Room reservedRoom = reservation.getReservedRoom();
        if (reservedRoom != null) {
            reservedRoom.setStatusToOccupied();
            dataStore.saveRoom(reservedRoom);
        }

        return new CheckInOutResponse(true, "チェックインが完了しました。部屋番号: " + reservedRoom.getRoomNumber());
    }

    public String getReservationInfo(CheckInOutRequest request) {
        Optional<Reservation> reservationOpt = dataStore.findReservationById(request.getReservationId());
        return reservationOpt.map(Reservation::getReservationInfo).orElse("予約情報が見つかりません。");
    }

    public String getReservationStatusInfo(CheckInOutRequest request) {
        Optional<Reservation> reservationOpt = dataStore.findReservationById(request.getReservationId());
        return reservationOpt.map(r -> r.getStatus().name()).orElse("予約情報が見つかりません。");
    }
}