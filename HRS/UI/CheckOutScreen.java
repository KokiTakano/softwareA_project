package HRS.UI;

import HRS.application.CheckOutControl;
import HRS.dto.CheckInOutRequest;
import HRS.dto.CheckInOutResponse;
import java.util.Scanner;
import java.math.BigDecimal;

public class CheckOutScreen {
    private final CheckOutControl checkOutControl;
    private final Scanner scanner;

    public CheckOutScreen(CheckOutControl checkOutControl) {
        this.checkOutControl = checkOutControl;
        this.scanner = new Scanner(System.in);
    }

    public void startCheckOutProcess() {
        System.out.println("\n--- チェックアウトを開始します ---");
        //System.out.print("予約番号を入力してください: ");
        //String reservationId = scanner.nextLine();
        System.out.print("部屋番号を入力してください: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        CheckInOutRequest request = new CheckInOutRequest(null, roomNumber);

        CheckInOutResponse accountingResponse = checkOutControl.processAccounting(request);
        if (!accountingResponse.isSuccess()) {
            System.out.println("会計情報取得に失敗しました: " + accountingResponse.getMessage());
            return;
        }

        BigDecimal amountDue = accountingResponse.getAmountDue();
        System.out.println("お支払い総額: " + amountDue);

        if (amountDue.compareTo(BigDecimal.ZERO) > 0) {
            System.out.print("支払いをしますか？ (yes/no): ");
            String payConfirm = scanner.nextLine();

            if (payConfirm.equalsIgnoreCase("yes")) {
                System.out.print("支払う金額を入力してください: ");
                BigDecimal amountPaid = new BigDecimal(scanner.nextLine());
                
                CheckInOutResponse checkOutResponse = checkOutControl.processCheckOut(request, amountPaid);
                if (checkOutResponse.isSuccess()) {
                    System.out.println(checkOutResponse.getMessage());
                    System.out.println("チェックアウトは完了です。");
                } else {
                    System.out.println("チェックアウト失敗: " + checkOutResponse.getMessage());
                }
            } else {
                System.out.println("支払いがキャンセルされました。チェックアウトできません。");
            }
        } else {
            System.out.println("未払い額はありません。チェックアウトを進めます。");
            CheckInOutResponse checkOutResponse = checkOutControl.processCheckOut(request, BigDecimal.ZERO);
            if (checkOutResponse.isSuccess()) {
                System.out.println("チェックアウトは完了です。");
            } else {
                System.out.println("チェックアウト失敗: " + checkOutResponse.getMessage());
            }
        }
    }
}