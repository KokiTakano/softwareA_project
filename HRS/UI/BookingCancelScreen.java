package HRS.UI;

import HRS.application.ReservationCancelControl;
import HRS.dto.CheckInOutRequest;
import HRS.dto.CheckInOutResponse;
import java.util.Scanner;

public class BookingCancelScreen {
    private final ReservationCancelControl reservationCancelControl;
    private final Scanner scanner;

    public BookingCancelScreen(ReservationCancelControl reservationCancelControl) {
        this.reservationCancelControl = reservationCancelControl;
        this.scanner = new Scanner(System.in);
    }

    public void startCancelProcess() {
        System.out.println("\n--- 予約キャンセルを開始します ---");
        System.out.print("予約番号を入力してください: ");
        String reservationId = scanner.nextLine();

        CheckInOutRequest request = new CheckInOutRequest(reservationId);

        String reservationInfo = reservationCancelControl.getReservationInfo(request);
        System.out.println("予約情報: " + reservationInfo);

        if (reservationInfo.equals("予約情報が見つかりません。")) {
            return;
        }

        String reservationStatus = reservationCancelControl.getReservationStatusInfo(request);

        if (reservationStatus.equals("CHECKED_OUT")){
            System.out.println("既にチェックアウトされています。");
            return;
        }else if (reservationStatus.equals("CHECKED_IN")){
            System.out.println("既にチェックインされています。キャンセルは出来ません。");
            return;
        }else if (reservationStatus.equals("CANCEL")){
            System.out.println("既にキャンセルされています。");
            return;
        }

        System.out.print("予約をキャンセルしますか？ (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            CheckInOutResponse response = reservationCancelControl.processReservationCancel(request);
            if (response.isSuccess()) {
                System.out.println(response.getMessage());
            } else {
                System.out.println("予約キャンセル失敗: " + response.getMessage());
            }
        } else {
            System.out.println("予約はキャンセルされていません。");
        }
    }
}
