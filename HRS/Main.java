package HRS;

import HRS.UI.RoomBookingScreen;
import HRS.UI.CheckInScreen;
import HRS.UI.CheckOutScreen;
import HRS.UI.BookingCancelScreen;
import HRS.application.RoomReservationControl;
import HRS.application.CheckInControl;
import HRS.application.CheckOutControl;
import HRS.application.ReservationCancelControl;
import HRS.data.HotelDataStore;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HotelDataStore.getInstance();

        RoomReservationControl roomReservationControl = new RoomReservationControl();
        CheckInControl checkInControl = new CheckInControl();
        CheckOutControl checkOutControl = new CheckOutControl();
        ReservationCancelControl reservationCancelControl = new ReservationCancelControl();

        RoomBookingScreen roomBookingScreen = new RoomBookingScreen(roomReservationControl);
        CheckInScreen checkInScreen = new CheckInScreen(checkInControl);
        CheckOutScreen checkOutScreen = new CheckOutScreen(checkOutControl);
        BookingCancelScreen bookingCancelScreen = new BookingCancelScreen(reservationCancelControl);

        Scanner mainScanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- ホテル予約システム ---");
            System.out.println("1. 部屋を予約する");
            System.out.println("2. チェックインする");
            System.out.println("3. チェックアウトする");
            System.out.println("4.予約をキャンセルする");
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
                case "4":
                    bookingCancelScreen.startCancelProcess();    
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
