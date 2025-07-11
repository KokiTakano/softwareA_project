package HRS.application;

import HRS.data.HotelDataStore;
import HRS.dto.CheckInOutRequest;
import HRS.dto.CheckInOutResponse;
import HRS.domain.Payment;
import HRS.domain.Reservation;
import HRS.domain.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CheckOutControl {
    private final HotelDataStore dataStore;

    public CheckOutControl() {
        this.dataStore = HotelDataStore.getInstance();
    }

    public CheckInOutResponse processAccounting(CheckInOutRequest request) {
        Optional<Reservation> reservationOpt = dataStore.findCheckedInReservationByRoomNumber(request.getRoomNumber());
        if (!reservationOpt.isPresent()) {
            return new CheckInOutResponse(false, "指定された部屋は利用されていません。");
        }
        Reservation reservation = reservationOpt.get();

        Optional<Payment> existingPaymentOpt = dataStore.findPaymentByReservationId(reservation.getReservationId());
        BigDecimal paidAmount = existingPaymentOpt.map(Payment::getAmount).orElse(BigDecimal.ZERO);
        
        BigDecimal totalAmount = reservation.getTotalAmount();
        BigDecimal amountDue = totalAmount.subtract(paidAmount);

        return new CheckInOutResponse(true, "会計情報", amountDue);
    }

    public CheckInOutResponse processCheckOut(CheckInOutRequest request, BigDecimal amountPaid) {
        Optional<Reservation> reservationOpt = dataStore.findCheckedInReservationByRoomNumber(request.getRoomNumber());
        if (!reservationOpt.isPresent()) {
            return new CheckInOutResponse(false, "予約が見つかりません。");
        }
        Reservation reservation = reservationOpt.get();
        
        CheckInOutResponse accountingInfo = processAccounting(request);
        if (!accountingInfo.isSuccess() || accountingInfo.getAmountDue() == null) {
             return new CheckInOutResponse(false, "会計情報の取得に失敗しました。");
        }
        BigDecimal amountDueToPay = accountingInfo.getAmountDue();

        if (amountPaid.compareTo(amountDueToPay) < 0) {
            return new CheckInOutResponse(false, "支払い金額が不足しています。未払い額は" + amountDueToPay.subtract(amountPaid) + "円です。");
        }

        Optional<Payment> existingPaymentOpt = dataStore.findPaymentByReservationId(reservation.getReservationId());
        Payment payment;
        if (existingPaymentOpt.isPresent()) {
            payment = existingPaymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.PAID);
        } else {
            payment = new Payment(UUID.randomUUID().toString(), reservation.getReservationId(), amountPaid);
        }
        payment.setPaymentCompleted();
        dataStore.savePayment(payment);

        reservation.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        dataStore.saveReservation(reservation);

        Room room = reservation.getReservedRoom();
        if (room != null) {
            room.setStatusToVacant();
            dataStore.saveRoom(room);
        }

        if(amountPaid.compareTo(amountDueToPay) == 0){
            return new CheckInOutResponse(true, "ちょうどお預かりしました。お釣りはありません。");
        }else{
            return new CheckInOutResponse(true, "お釣りは" + amountPaid.subtract(amountDueToPay) + "円になります。");
        }
    }
}