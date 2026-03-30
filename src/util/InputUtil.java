package util;

import java.util.Scanner;

public class InputUtil {
    private static Scanner sc = new Scanner(System.in);

    public static int getInt() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số!");
            return getInt();
        }
    }

    public static int getInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số!");
            return getInt(scanner);
        }
    }

    public static String getString() {
        return sc.nextLine();
    }
}
