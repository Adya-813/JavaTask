import java.sql.*;
import java.sql.Date;
import java.util.*;

public class OnlineReservationSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3306/reservation_db";
    static final String DB_USER = "root";
    static final String DB_PASS = "Adyasha2003*";

    static Scanner sc = new Scanner(System.in);
    static String loggedInUser = null;

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            while (true) {
                System.out.println("\n--- ONLINE RESERVATION SYSTEM ---");
                System.out.println("1. Login");
                System.out.println("2. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    if (login(conn)) {
                        mainMenu(conn);
                    }
                } else {
                    System.out.println("Thank you for using the system.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login method
    static boolean login(Connection conn) throws SQLException {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                loggedInUser = username;
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Invalid credentials.");
                return false;
            }
        }
    }

    // Main menu after login
    static void mainMenu(Connection conn) throws SQLException {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Reservation");
            System.out.println("2. Cancellation");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> reservation(conn);
                case 2 -> cancellation(conn);
                case 3 -> {
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Reservation method
    static void reservation(Connection conn) throws SQLException {
        System.out.print("Enter Train Number: ");
        String trainNumber = sc.nextLine();
        System.out.print("Enter Train Name: ");
        String trainName = sc.nextLine();
        System.out.print("Enter Class Type: ");
        String classType = sc.nextLine();
        System.out.print("Enter Date of Journey (YYYY-MM-DD): ");
        String doj = sc.nextLine();
        System.out.print("From Place: ");
        String fromPlace = sc.nextLine();
        System.out.print("To Place: ");
        String toPlace = sc.nextLine();

        long pnr = System.currentTimeMillis(); // unique PNR

        String sql = "INSERT INTO reservations (pnr, username, train_number, train_name, class_type, date_of_journey, from_place, to_place) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, pnr);
            pst.setString(2, loggedInUser);
            pst.setString(3, trainNumber);
            pst.setString(4, trainName);
            pst.setString(5, classType);
            pst.setDate(6, Date.valueOf(doj));
            pst.setString(7, fromPlace);
            pst.setString(8, toPlace);
            pst.executeUpdate();
            System.out.println("Reservation successful! Your PNR is: " + pnr);
        }
    }

    // Cancellation method
    static void cancellation(Connection conn) throws SQLException {
        System.out.print("Enter PNR to cancel: ");
        long pnr = sc.nextLong();
        sc.nextLine();

        String sql = "SELECT * FROM reservations WHERE pnr=? AND username=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, pnr);
            pst.setString(2, loggedInUser);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("PNR: " + rs.getLong("pnr"));
                System.out.println("Train: " + rs.getString("train_number") + " - " + rs.getString("train_name"));
                System.out.println("From: " + rs.getString("from_place") + " To: " + rs.getString("to_place"));
                System.out.print("Confirm cancellation? (yes/no): ");
                String confirm = sc.nextLine();
                if (confirm.equalsIgnoreCase("yes")) {
                    try (PreparedStatement deletePst = conn.prepareStatement("DELETE FROM reservations WHERE pnr=?")) {
                        deletePst.setLong(1, pnr);
                        deletePst.executeUpdate();
                        System.out.println("Ticket cancelled successfully.");
                    }
                } else {
                    System.out.println("Cancellation aborted.");
                }
            } else {
                System.out.println("No booking found for given PNR.");
            }
        }
    }
}
