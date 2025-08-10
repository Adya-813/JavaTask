import java.util.*;

public class OnlineExamSystem {
    static int userId = 1234;
    static int password = 4321;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        if (login()) {
            updateProfile();
            startExam();
            logout();
        }
    }

    static boolean login() {
        System.out.println("=== LOGIN ===");
        System.out.print("Enter User ID (integer): ");
        int id = sc.nextInt();
        System.out.print("Enter Password (integer): ");
        int pass = sc.nextInt();

        if (id == userId && pass == password) {
            System.out.println("Login successful!\n");
            return true;
        } else {
            System.out.println("Invalid credentials. Exiting...");
            return false;
        }
    }

    static void updateProfile() {
        System.out.println("\nDo you want to update your profile? (yes/no): ");
        sc.nextLine(); // consume newline
        String choice = sc.nextLine().trim().toLowerCase();
        if (choice.equals("yes")) {
            System.out.print("Enter new User ID: ");
            userId = sc.nextInt();
            System.out.print("Enter new Password: ");
            password = sc.nextInt();
            System.out.println("Profile updated successfully!\n");
        }
    }

    static void startExam() {
        String[] questions = {
            "Q1: What is the capital of France?\n1) Berlin  2) Madrid  3) Paris  4) Rome",
            "Q2: Which is the largest planet?\n1) Earth  2) Jupiter  3) Mars  4) Saturn",
            "Q3: 5 + 3 = ?\n1) 5  2) 8  3) 10  4) 6"
        };

        int[] correctAnswers = {3, 2, 2}; // correct options
        int score = 0;

        System.out.println("=== EXAM STARTS NOW ===");
        int timeLimit = 30; // seconds for the exam
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < questions.length; i++) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsedTime >= timeLimit) {
                System.out.println("\nTime is up! Auto-submitting...");
                break;
            }

            System.out.println("\n" + questions[i]);
            System.out.print("Your answer: ");
            int ans = sc.nextInt();

            if (ans == correctAnswers[i]) {
                System.out.println("✅ Correct");
                score++;
            } else {
                System.out.println("❌ Wrong (Correct answer: " + correctAnswers[i] + ")");
            }
        }

        System.out.println("\n=== RESULT ===");
        System.out.println("Score: " + score + "/" + questions.length);
        if (score >= 2) {
            System.out.println("🎉 Pass");
        } else {
            System.out.println("❌ Fail");
        }
    }

    static void logout() {
        System.out.println("\nLogging out and closing session...");
        System.out.println("Thank you for attending the exam!");
    }
}
