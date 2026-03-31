package util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Get integer input from default scanner
     */
    public static int getInt() {
        try {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Vui lòng nhập số!");
                return getInt();
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số hợp lệ!");
            return getInt();
        }
    }

    /**
     * Get integer input from provided scanner
     */
    public static int getInt(Scanner scanner) {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Vui lòng nhập số!");
                return getInt(scanner);
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số hợp lệ!");
            return getInt(scanner);
        }
    }

    /**
     * Get string input from default scanner
     */
    public static String getString() {
        return sc.nextLine().trim();
    }

    /**
     * Get positive integer (greater than 0)
     */
    public static int getPositiveInt(Scanner scanner) {
        int value;
        do {
            value = getInt(scanner);
            if (value <= 0) {
                System.out.println("Vui lòng nhập số lớn hơn 0!");
            }
        } while (value <= 0);
        return value;
    }
}
