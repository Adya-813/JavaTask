import javax.swing.*;
import java.io.*;
import java.util.*;

public class NumberGuessingGameGUI {
    private static final int MAX_ATTEMPTS = 7;
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private static Map<String, Integer> leaderboard = new HashMap<>();

    public static void main(String[] args) {
        loadLeaderboard();
        JOptionPane.showMessageDialog(null, "🎯 Welcome to the Number Guessing Game!\nYou have " + MAX_ATTEMPTS + " attempts per round.");
        
        String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) playerName = "Player";

        boolean playAgain = true;
        while (playAgain) {
            int score = playRound();
            leaderboard.put(playerName, Math.max(score, leaderboard.getOrDefault(playerName, 0)));
            saveLeaderboard();
            showLeaderboard();
            
            int option = JOptionPane.showConfirmDialog(null, "Do you want to play another round?", "Play Again", JOptionPane.YES_NO_OPTION);
            playAgain = (option == JOptionPane.YES_OPTION);
        }

        JOptionPane.showMessageDialog(null, "Thanks for playing! 🎉");
    }

    private static int playRound() {
        int numberToGuess = new Random().nextInt(100) + 1;
        int attempts = 0;
        int score = 100;
        
        while (attempts < MAX_ATTEMPTS) {
            String guessStr = JOptionPane.showInputDialog("Guess the number (1-100):\nHint: " + getHint(numberToGuess));
            if (guessStr == null) break; // Cancel
            int guess;
            try {
                guess = Integer.parseInt(guessStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "❌ Invalid input. Please enter a number.");
                continue;
            }
            
            attempts++;
            if (guess == numberToGuess) {
                JOptionPane.showMessageDialog(null, "🎉 Correct! You guessed it in " + attempts + " attempts.");
                score -= (attempts - 1) * 10;
                return Math.max(score, 0);
            } else if (guess < numberToGuess) {
                JOptionPane.showMessageDialog(null, "📉 Too low!");
            } else {
                JOptionPane.showMessageDialog(null, "📈 Too high!");
            }
        }

        JOptionPane.showMessageDialog(null, "😢 Out of attempts! The number was: " + numberToGuess);
        return 0;
    }

    private static String getHint(int number) {
        if (number % 2 == 0) return "The number is even.";
        if (number % 5 == 0) return "The number is divisible by 5.";
        if (number <= 50) return "The number is between 1 and 50.";
        return "The number is between 51 and 100.";
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
            JOptionPane.showMessageDialog(null, "Error saving leaderboard.");
        }
    }

    private static void showLeaderboard() {
        StringBuilder sb = new StringBuilder("🏆 Leaderboard 🏆\n");
        leaderboard.entrySet().stream()
            .sorted((a, b) -> b.getValue() - a.getValue())
            .forEach(entry -> sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
