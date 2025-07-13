package HRS.application;

import HRS.data.HotelDataStore;
import HRS.dto.CheckInOutRequest;
import HRS.dto.CheckInOutResponse;
import HRS.domain.Reservation;

import java.util.Optional;

public class ReservationCancelControl {
    private final HotelDataStore dataStore;

    public ReservationCancelControl() {
        this.dataStore = HotelDataStore.getInstance();
    }

    public CheckInOutResponse processReservationCancel(CheckInOutRequest request) {
        Optional<Reservation> reservationOpt = dataStore.findReservationById(request.getReservationId());

        if (!reservationOpt.isPresent()) {
            return new CheckInOutResponse(false, "予約番号が見つかりません。");
        }

        Reservation reservation = reservationOpt.get();

        reservation.setReservationCancel();
        dataStore.saveReservation(reservation);

        return new CheckInOutResponse(true, "予約をキャンセルしました。");
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