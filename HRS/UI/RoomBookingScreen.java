package HRS.UI;

import HRS.application.RoomReservationControl;
import HRS.dto.ReservationRequest;
import HRS.dto.ReservationResponse;
import HRS.dto.RoomSearchRequest;
import HRS.dto.RoomTypeResult;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class RoomBookingScreen {
    private final RoomReservationControl roomReservationControl;
    private final Scanner scanner;

    public RoomBookingScreen(RoomReservationControl roomReservationControl) {
        this.roomReservationControl = roomReservationControl;
        this.scanner = new Scanner(System.in);
    }

    public void startBookingProcess() {
        System.out.println("--- 部屋予約を開始します ---");

        System.out.print("チェックイン日 (YYYY-MM-DD): ");
        LocalDate checkInDate;
        try{
            checkInDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e){
            System.err.println("エラー: チェックイン日の形式が正しくありません。YYYY-MM-DD形式で入力してください。");
            return; // エラーが発生したため、ここでメソッドを終了
        }

        if(checkInDate.isBefore(LocalDate.now())){
            System.err.println("エラー: 過去の日付が選択されました。今日以降の日付を入力してください。");
            return; // エラーが発生したため、ここでメソッドを終了
        }

        System.out.print("宿泊日数: ");
        int stayDays = Integer.parseInt(scanner.nextLine());

        String desiredRoomType = null;

        RoomSearchRequest searchRequest = new RoomSearchRequest(checkInDate, stayDays, desiredRoomType);

        List<RoomTypeResult> availableRoomTypes = roomReservationControl.searchAvailableRooms(searchRequest);

        if (availableRoomTypes.isEmpty()) {
            System.out.println("申し訳ありません。ご希望の期間と条件で利用可能な部屋はありませんでした。");
            return;
        }

        System.out.println("\n--- 利用可能な部屋の種類 ---");
        for (int i = 0; i < availableRoomTypes.size(); i++) {
            RoomTypeResult result = availableRoomTypes.get(i);
            System.out.println((i + 1) + ". 種類:" + result.getRoomType() + "(" + (i + 1) + "), 空き部屋数:" + result.getAvailableCount());
        }

        System.out.print("予約したい部屋の種類の番号を選択してください: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice < 0 || choice >= availableRoomTypes.size()) {
            System.out.println("無効な選択です。");
            return;
        }
        int selectedSampleRoomNumber = availableRoomTypes.get(choice).getSampleRoomNumber();

        ReservationRequest reservationRequest = new ReservationRequest(
            selectedSampleRoomNumber,
            checkInDate,
            stayDays
        );
        ReservationResponse response = roomReservationControl.makeReservation(reservationRequest);

        if (response.isSuccess()) {
            System.out.println("\n--- 予約が完了しました ---");
            System.out.println("予約番号: " + response.getReservationId());
            System.out.println("合計金額: " + response.getTotalAmount());
            System.out.println("※お支払いはチェックアウト時に行います。");
        } else {
            System.out.println("予約に失敗しました: " + response.getMessage());
        }
    }
}