import java.io.*;
import java.util.*;

public class ATMWithUserBalanceConsole {

    private static final String FILE_NAME = "users.txt";
    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Map<String, User> users = loadUsers();

            System.out.print(CYAN + "Enter User ID: " + RESET);
            String userId = sc.nextLine();
            System.out.print(CYAN + "Enter PIN: " + RESET);
            String pin = sc.nextLine();

            if (users.containsKey(userId)) {
                User user = users.get(userId);
                if (user.pin.equals(pin)) {
                    System.out.println(GREEN + "Login successful! Welcome back, " + userId + RESET);
                    showATMMenu(sc, user, users);
                } else {
                    System.out.println(RED + "Invalid PIN. Access Denied." + RESET);
                }
            } else {
                User newUser = new User(userId, pin, 1000.00);
                users.put(userId, newUser);
                saveUsers(users);
                System.out.println(GREEN + "New account created! Welcome, " + userId + RESET);
                showATMMenu(sc, newUser, users);
            }

        } catch (IOException e) {
            System.out.println(RED + "Error: " + e.getMessage() + RESET);
        }
    }

    private static Map<String, User> loadUsers() throws IOException {
        Map<String, User> users = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String id = parts[0];
                        String pin = parts[1];
                        double balance = Double.parseDouble(parts[2]);
                        users.put(id, new User(id, pin, balance));
                    }
                }
            }
        }
        return users;
    }

    private static void saveUsers(Map<String, User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users.values()) {
                bw.write(user.userId + "," + user.pin + "," + user.balance);
                bw.newLine();
            }
        }
    }

    private static void showATMMenu(Scanner sc, User currentUser, Map<String, User> users) {
        while (true) {
            System.out.println(BLUE + "\n===== ATM Menu =====" + RESET);
            System.out.println(YELLOW + "1. Check Balance" + RESET);
            System.out.println(YELLOW + "2. Deposit" + RESET);
            System.out.println(YELLOW + "3. Withdraw" + RESET);
            System.out.println(YELLOW + "4. Exit" + RESET);
            System.out.print(PURPLE + "Select an option: " + RESET);

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.println(CYAN + "Current Balance: $" + currentUser.balance + RESET);
                    break;
                case "2":
                    System.out.print(CYAN + "Enter deposit amount: " + RESET);
                    try {
                        double dep = Double.parseDouble(sc.nextLine());
                        if (dep > 0) {
                            currentUser.balance += dep;
                            saveAndConfirm(users);
                        } else {
                            System.out.println(RED + "Invalid amount!" + RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "Invalid input!" + RESET);
                    }
                    break;
                case "3":
                    System.out.print(CYAN + "Enter withdrawal amount: " + RESET);
                    try {
                        double with = Double.parseDouble(sc.nextLine());
                        if (with > 0 && with <= currentUser.balance) {
                            currentUser.balance -= with;
                            saveAndConfirm(users);
                        } else {
                            System.out.println(RED + "Insufficient funds or invalid amount!" + RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(RED + "Invalid input!" + RESET);
                    }
                    break;
                case "4":
                    saveAndConfirm(users);
                    System.out.println(GREEN + "Goodbye, " + currentUser.userId + "!" + RESET);
                    return;
                default:
                    System.out.println(RED + "Invalid choice!" + RESET);
            }
        }
    }

    private static void saveAndConfirm(Map<String, User> users) {
        try {
            saveUsers(users);
            System.out.println(GREEN + "Transaction successful! Data saved." + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error saving data: " + e.getMessage() + RESET);
        }
    }

    static class User {
        String userId;
        String pin;
        double balance;

        User(String userId, String pin, double balance) {
            this.userId = userId;
            this.pin = pin;
            this.balance = balance;
        }
    }
}
