import java.sql.*;
import java.util.Scanner;

public class ReservationSystem {
    static final String URL = "jdbc:mysql://localhost:3306/reservation_system";
    static final String USER = "root";     // change to your MySQL username
    static final String PASS = "Adyasha2003*";     // change to your MySQL password

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("==== ONLINE RESERVATION SYSTEM ====");

            if (login(conn)) {
                int choice;
                do {
                    System.out.println("\n--- Main Menu ---");
                    System.out.println("1. Reservation");
                    System.out.println("2. Cancellation");
                    System.out.println("3. Exit");
                    System.out.print("Enter choice: ");
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1 -> makeReservation(conn);
                        case 2 -> cancelReservation(conn);
                        case 3 -> System.out.println("Thank you for using the system.");
                        default -> System.out.println("Invalid choice!");
                    }
                } while (choice != 3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------- LOGIN FORM --------
    static boolean login(Connection conn) throws SQLException {
        System.out.println("\nLogin Form:");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login Successful!");
                    return true;
                } else {
                    System.out.println("Invalid Login ID or Password.");
                    return false;
                }
            }
        }
    }

    // -------- RESERVATION FORM --------
    static void makeReservation(Connection conn) throws SQLException {
        System.out.println("\nReservation Form:");
        System.out.print("Enter Passenger Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Train Number: ");
        String trainNo = sc.nextLine();
        System.out.print("Enter Class Type (Sleeper/AC/General): ");
        String classType = sc.nextLine();
        System.out.print("Enter Date of Journey (yyyy-mm-dd): ");
        String date = sc.nextLine();
        System.out.print("Enter From: ");
        String from = sc.nextLine();
        System.out.print("Enter To: ");
        String to = sc.nextLine();

        String sql = "INSERT INTO reservations (passenger_name, train_number, class_type, journey_date, source, destination) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, name);
            pst.setString(2, trainNo);
            pst.setString(3, classType);
            pst.setDate(4, Date.valueOf(date));
            pst.setString(5, from);
            pst.setString(6, to);

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    int pnr = rs.getInt(1);
                    System.out.println("Reservation Successful! Your PNR: " + pnr);
                }
            }
        }
    }

    // -------- CANCELLATION FORM --------
    static void cancelReservation(Connection conn) throws SQLException {
        System.out.println("\nCancellation Form:");
        System.out.print("Enter PNR Number: ");
        int pnr = sc.nextInt();
        sc.nextLine();

        String sql = "SELECT * FROM reservations WHERE pnr=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, pnr);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Booking Details: " +
                        "Name=" + rs.getString("passenger_name") +
                        ", Train=" + rs.getString("train_number") +
                        ", Date=" + rs.getDate("journey_date"));

                    System.out.print("Do you really want to cancel? (yes/no): ");
                    String confirm = sc.nextLine();
                    if (confirm.equalsIgnoreCase("yes")) {
                        // Insert into cancellations
                        String cancelSql = "INSERT INTO cancellations (pnr) VALUES (?)";
                        try (PreparedStatement cpst = conn.prepareStatement(cancelSql)) {
                            cpst.setInt(1, pnr);
                            cpst.executeUpdate();
                        }
                        // Delete from reservations
                        String delSql = "DELETE FROM reservations WHERE pnr=?";
                        try (PreparedStatement dpst = conn.prepareStatement(delSql)) {
                            dpst.setInt(1, pnr);
                            dpst.executeUpdate();
                        }
                        System.out.println("Ticket Cancelled Successfully!");
                    } else {
                        System.out.println("Cancellation Aborted.");
                    }
                } else {
                    System.out.println("Invalid PNR Number!");
                }
            }
        }
    }
}
