package HRS.UI;

import HRS.application.CheckInControl;
import HRS.dto.CheckInOutRequest;
import HRS.dto.CheckInOutResponse;
import java.util.Scanner;

public class CheckInScreen {
    private final CheckInControl checkInControl;
    private final Scanner scanner;

    public CheckInScreen(CheckInControl checkInControl) {
        this.checkInControl = checkInControl;
        this.scanner = new Scanner(System.in);
    }

    public void startCheckInProcess() {
        System.out.println("\n--- チェックインを開始します ---");
        System.out.print("予約番号を入力してください: ");
        String reservationId = scanner.nextLine();

        CheckInOutRequest request = new CheckInOutRequest(reservationId);

        System.out.println("予約情報: " + checkInControl.getReservationInfo(request));

        System.out.print("チェックインを続行しますか？ (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            CheckInOutResponse response = checkInControl.processCheckIn(request);
            if (response.isSuccess()) {
                System.out.println(response.getMessage());
            } else {
                System.out.println("チェックイン失敗: " + response.getMessage());
            }
        } else {
            System.out.println("チェックインをキャンセルしました。");
        }
    }
}