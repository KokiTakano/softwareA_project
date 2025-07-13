package HRS.data;

import HRS.domain.Payment;
import HRS.domain.Reservation;
import HRS.domain.Room;
import HRS.domain.NormalRoom;
import HRS.domain.SuiteRoom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HotelDataStore {
    private static final HotelDataStore instance = new HotelDataStore();

    public final Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    public final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    public final Map<String, Payment> payments = new ConcurrentHashMap<>();

    private HotelDataStore() {
        // 初期データ
        rooms.put(101, new NormalRoom(101, new BigDecimal("10000")));
        rooms.put(102, new NormalRoom(102, new BigDecimal("10000")));
        rooms.put(201, new SuiteRoom(201, new BigDecimal("50000")));
        rooms.put(202, new SuiteRoom(202, new BigDecimal("50000")));
        rooms.get(102).setStatus(Room.RoomStatus.OCCUPIED); // 例として102を使用中に
    }

    public static HotelDataStore getInstance() {
        return instance;
    }

    // --- Roomに関する操作 ---
    public Optional<Room> findRoomByRoomNumber(int roomNumber) {
        return Optional.ofNullable(rooms.get(roomNumber));
    }

    public List<Room> findAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> findRoomsByStatus(Room.RoomStatus status) {
        return rooms.values().stream()
                .filter(room -> room.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void saveRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    // --- Reservationに関する操作 ---
    public Optional<Reservation> findReservationById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    public List<Reservation> findAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> findReservationsByRoomAndPeriod(Room room, LocalDate checkInDate, int stayDurationDays) {
        LocalDate desiredPeriodEnd = checkInDate.plusDays(stayDurationDays - 1);

        return reservations.values().stream()
                .filter(r -> r.getReservedRoom().getRoomNumber() == room.getRoomNumber())
                .filter(r -> r.getStatus() == Reservation.ReservationStatus.CONFIRMED || r.getStatus() == Reservation.ReservationStatus.CHECKED_IN)
                .filter(r -> {
                    LocalDate existingReservationEnd = r.getCheckInDate().plusDays(r.getStayDurationDays() - 1);
                    // 期間が重複しない条件: (既存予約の終了 < 希望期間の開始) OR (希望期間の終了 < 既存予約の開始)
                    return !(existingReservationEnd.isBefore(checkInDate) || desiredPeriodEnd.isBefore(r.getCheckInDate()));
                })
                .collect(Collectors.toList());
    }

    public void saveReservation(Reservation reservation) {
        reservations.put(reservation.getReservationId(), reservation);
    }

    public Optional<Reservation> findCheckedInReservationByRoomNumber(int roomNumber) {
        // 1. 指定された部屋番号の部屋を取得
        Optional<Room> roomOpt = findRoomByRoomNumber(roomNumber);
        if (!roomOpt.isPresent()) {
            return Optional.empty(); // 部屋が見つからない
        }
        Room room = roomOpt.get();

        // 2. その部屋が「使用中」であることを確認
        if (room.getStatus() != Room.RoomStatus.OCCUPIED) {
            // 使用中でない場合、チェックアウト対象の予約はない
            return Optional.empty();
        }

        // 3. その部屋に紐づく、現在「CHECKED_IN」状態の予約を探す
        return reservations.values().stream()
                           .filter(r -> r.getReservedRoom().getRoomNumber() == roomNumber)
                           .filter(r -> r.getStatus() == Reservation.ReservationStatus.CHECKED_IN)
                           .findFirst();
    }

    // --- Paymentに関する操作 ---
    public Optional<Payment> findPaymentById(String paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }

    public Optional<Payment> findPaymentByReservationId(String reservationId) {
        return payments.values().stream()
                .filter(p -> p.getReservationId().equals(reservationId))
                .findFirst();
    }

    public void savePayment(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
    }
}