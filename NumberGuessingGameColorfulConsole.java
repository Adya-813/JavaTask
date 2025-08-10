import java.io.*;
import java.util.*;

public class NumberGuessingGameColorfulConsole {

    private static final int MAX_ATTEMPTS = 7;
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private static Map<String, Integer> leaderboard = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        loadLeaderboard();
        System.out.println(BLUE + "🎯 Welcome to the Number Guessing Game!" + RESET);
        System.out.println(CYAN + "You have " + MAX_ATTEMPTS + " attempts per round.\n" + RESET);

        System.out.print(GREEN + "Enter your name: " + RESET);
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) playerName = "Player";

        boolean playAgain = true;
        while (playAgain) {
            int score = playRound();
            leaderboard.put(playerName, Math.max(score, leaderboard.getOrDefault(playerName, 0)));
            saveLeaderboard();
            showLeaderboard();

            System.out.print("\n" + YELLOW + "Do you want to play another round? (y/n): " + RESET);
            playAgain = scanner.nextLine().trim().equalsIgnoreCase("y");
        }

        System.out.println(GREEN + "\nThanks for playing! 🎉" + RESET);
    }

    private static int playRound() {
        int numberToGuess = new Random().nextInt(100) + 1;
        int attempts = 0;
        int score = 100;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("\n" + CYAN + "Guess the number (1-100): " + RESET + "Hint → " + getHint(numberToGuess) + "\nYour guess: ");
            String guessStr = scanner.nextLine();
            int guess;
            try {
                guess = Integer.parseInt(guessStr);
            } catch (NumberFormatException e) {
                System.out.println(RED + "❌ Invalid input. Please enter a number." + RESET);
                continue;
            }

            attempts++;
            if (guess == numberToGuess) {
                System.out.println(GREEN + "🎉 Correct! You guessed it in " + attempts + " attempts." + RESET);
                score -= (attempts - 1) * 10;
                return Math.max(score, 0);
            } else if (guess < numberToGuess) {
                System.out.println(YELLOW + "📉 Too low!" + RESET);
            } else {
                System.out.println(YELLOW + "📈 Too high!" + RESET);
            }
        }

        System.out.println(RED + "😢 Out of attempts! The number was: " + numberToGuess + RESET);
        return 0;
    }

    private static String getHint(int number) {
        if (number % 2 == 0) return GREEN + "The number is even." + RESET;
        if (number % 5 == 0) return CYAN + "The number is divisible by 5." + RESET;
        if (number <= 50) return YELLOW + "The number is between 1 and 50." + RESET;
        return PURPLE + "The number is between 51 and 100." + RESET;
    }

    private static void loadLeaderboard() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    leaderboard.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException ignored) {}
    }

    private static void saveLeaderboard() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(RED + "Error saving leaderboard." + RESET);
        }
    }

    private static void showLeaderboard() {
        System.out.println(BLUE + "\n🏆 Leaderboard 🏆" + RESET);
        leaderboard.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .forEach(entry -> System.out.println(GREEN + entry.getKey() + ": " + entry.getValue() + RESET));
    }
}
