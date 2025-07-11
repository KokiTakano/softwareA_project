package HRS;

import HRS.UI.RoomBookingScreen;
import HRS.UI.CheckInScreen;
import HRS.UI.CheckOutScreen;
import HRS.application.RoomReservationControl;
import HRS.application.CheckInControl;
import HRS.application.CheckOutControl;
import HRS.data.HotelDataStore;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HotelDataStore.getInstance();

        RoomReservationControl roomReservationControl = new RoomReservationControl();
        CheckInControl checkInControl = new CheckInControl();
        CheckOutControl checkOutControl = new CheckOutControl();

        RoomBookingScreen roomBookingScreen = new RoomBookingScreen(roomReservationControl);
        CheckInScreen checkInScreen = new CheckInScreen(checkInControl);
        CheckOutScreen checkOutScreen = new CheckOutScreen(checkOutControl);

        Scanner mainScanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- ホテル予約システム ---");
            System.out.println("1. 部屋を予約する");
            System.out.println("2. チェックインする");
            System.out.println("3. チェックアウトする");
            System.out.println("0. 終了");
            System.out.print("選択してください: ");

            String choice = mainScanner.nextLine();

            switch (choice) {
                case "1":
                    roomBookingScreen.startBookingProcess();
                    break;
                case "2":
                    checkInScreen.startCheckInProcess();
                    break;
                case "3":
                    checkOutScreen.startCheckOutProcess();
                    break;
                case "0":
                    System.out.println("システムを終了します。");
                    mainScanner.close();
                    return;
                default:
                    System.out.println("無効な選択です。");
            }
        }
    }
}
