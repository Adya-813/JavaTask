import java.util.*;

class MCQ {
    String question;
    String[] options;
    char correctAnswer;

    public MCQ(String question, String[] options, char correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class MCQTestSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Login
        System.out.print("Enter User ID (integer): ");
        int userId = sc.nextInt();
        System.out.print("Enter Password (integer): ");
        int password = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.println("\nWelcome User " + userId + "!\n");

        // Load hardcoded questions
        List<MCQ> questions = new ArrayList<>();
        questions.add(new MCQ("What is the capital of France?",
                new String[]{"A. Paris", "B. London", "C. Rome", "D. Berlin"}, 'A'));
        questions.add(new MCQ("Which planet is known as the Red Planet?",
                new String[]{"A. Venus", "B. Mars", "C. Jupiter", "D. Saturn"}, 'B'));
        questions.add(new MCQ("Who developed the theory of relativity?",
                new String[]{"A. Newton", "B. Einstein", "C. Galileo", "D. Tesla"}, 'B'));

        // Quiz
        int score = 0;
        for (MCQ mcq : questions) {
            System.out.println("\n" + mcq.question);
            for (String option : mcq.options) {
                System.out.println(option);
            }
            System.out.print("Your answer (A/B/C/D): ");
            String answer = sc.nextLine().trim().toUpperCase();

            if (answer.length() > 0 && answer.charAt(0) == mcq.correctAnswer) {
                System.out.println("✅ Correct!");
                score += 10;
            } else {
                System.out.println("❌ Wrong! Correct answer: " + mcq.correctAnswer);
            }
        }

        // Result
        int totalPossibleScore = questions.size() * 10;
        int percentage = (score * 100) / totalPossibleScore;
        System.out.println("\nYour Score: " + score + "/" + totalPossibleScore + " (" + percentage + "%)");

        if (percentage >= 50) {
            System.out.println("Result: PASS ✅");
        } else {
            System.out.println("Result: FAIL ❌");
        }

        sc.close();
    }
}
