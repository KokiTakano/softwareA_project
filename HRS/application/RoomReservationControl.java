package HRS.application;

import HRS.data.HotelDataStore;
import HRS.dto.ReservationRequest;
import HRS.dto.ReservationResponse;
import HRS.dto.RoomSearchRequest;
import HRS.dto.RoomTypeResult;
import HRS.domain.Reservation;
import HRS.domain.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoomReservationControl {
    private final HotelDataStore dataStore;

    public RoomReservationControl() {
        this.dataStore = HotelDataStore.getInstance();
    }

    public List<RoomTypeResult> searchAvailableRooms(RoomSearchRequest request) {
        List<Room> allRooms = dataStore.findAllRooms();
        List<Room> physicallyAvailableRooms = allRooms.stream()
                .filter(room -> room.getStatus() == Room.RoomStatus.VACANT)
                .collect(Collectors.toList());

        List<Room> trulyAvailableRooms = new ArrayList<>();

        for (Room room : physicallyAvailableRooms) {
            List<Reservation> existingReservations = dataStore.findReservationsByRoomAndPeriod(room, request.getCheckInDate(), request.getStayDurationDays());

            boolean isRoomAvailable = existingReservations.isEmpty();

            if (request.getDesiredRoomType() != null && !request.getDesiredRoomType().isEmpty()) {
                isRoomAvailable = isRoomAvailable && room.getRoomType().equalsIgnoreCase(request.getDesiredRoomType());
            }

            if (isRoomAvailable) {
                trulyAvailableRooms.add(room);
            }
        }

        Map<String, List<Room>> roomsGroupedByType = trulyAvailableRooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomType));

        List<RoomTypeResult> results = new ArrayList<>();
        for (Map.Entry<String, List<Room>> entry : roomsGroupedByType.entrySet()) {
            int sampleRoomNumber = entry.getValue().stream()
                    .map(Room::getRoomNumber)
                    .findFirst()
                    .orElse(0);

            results.add(new RoomTypeResult(entry.getKey(), (long) entry.getValue().size(), sampleRoomNumber));
        }

        return results;
    }

    public ReservationResponse makeReservation(ReservationRequest request) {
        Optional<Room> selectedRoomOpt = dataStore.findRoomByRoomNumber(request.getRoomNumber());
        if (!selectedRoomOpt.isPresent()) {
            return new ReservationResponse(null, null, false, "指定された部屋が見つかりません。");
        }
        Room selectedRoom = selectedRoomOpt.get();

        if (selectedRoom.getStatus() != Room.RoomStatus.VACANT) {
             return new ReservationResponse(null, null, false, "指定された部屋は現在予約できません。");
        }
        List<Reservation> overlappingReservations = dataStore.findReservationsByRoomAndPeriod(selectedRoom, request.getCheckInDate(), request.getStayDurationDays());
        if (!overlappingReservations.isEmpty()) {
            return new ReservationResponse(null, null, false, "申し訳ありません。この期間は既に予約済みです。");
        }

        String reservationId = UUID.randomUUID().toString();
        BigDecimal totalAmount = selectedRoom.getPricePerNight().multiply(BigDecimal.valueOf(request.getStayDurationDays()));

        Reservation newReservation = new Reservation(
                reservationId,
                selectedRoom,
                request.getCheckInDate(),
                request.getStayDurationDays(),
                totalAmount
        );
        dataStore.saveReservation(newReservation);

        return new ReservationResponse(reservationId, totalAmount, true, "予約が完了しました。(支払いはチェックアウト時)");
    }
}
