import java.util.List;
import java.util.ArrayList;
public class GameLogic {

    public int validateGuess(String wordToGuess, Game game) {
        boolean isGuessed = wordToGuess.contains(String.valueOf(game.guess));
        int remaining = game.remainingGuesses;
        System.out.println("remaining in valid: " + remaining);

        if (!isGuessed) {
            remaining = game.remainingGuesses - 1;
        }
        return remaining;
    }

    public boolean validateUserGuess(String wordToGuess, Game game) {
        boolean isGuessed = wordToGuess.contains(String.valueOf(game.guess));
        if (!isGuessed) {
            return false;
        }
        return true;
    }

    public boolean isWordGuessed (Game game, String guessWord) {
        for (char letter : guessWord.toCharArray()) {
            if (!game.guessedLetters.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    public int wordLength (String wordToGuess, Game game) {
        int len = wordToGuess.length();
        return len;
    }

    public List<Integer> position (String wordToGuess, Game game) {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == game.guess) {
                positions.add(i);
            }
        }
        return positions;
    }

    public void attemptLeft (Game game, String guessWord) {
        if (isWordGuessed (game, guessWord) && game.category.getCategoryName().equals("ANIMALS")) {
            game.animaAttempt = 0;
        }
        else if (isWordGuessed (game, guessWord) && game.category.getCategoryName().equals("FRUITS")) {
            game.fruitsAttempt = 0;
        }
        else if (isWordGuessed (game, guessWord) && game.category.getCategoryName().equals("COUNTRIES")) {
            game.countryAttempt = 0;
        }
        else if (game.animaAttempt > 0 && game.category.getCategoryName().equals("ANIMALS")) {
            game.animaAttempt -= 1;
        }
        else if (game.fruitsAttempt > 0 && game.category.getCategoryName().equals("FRUITS")) {
            game.fruitsAttempt -= 1;
        }
        else if (game.countryAttempt > 0 && game.category.getCategoryName().equals("COUNTRIES")) {
            game.countryAttempt -= 1;
        }
    }


    public void restartGame (Game game) {
        game.category.setCategoryName("");
        game.guess = 0;
        game.startNewGame();
    }
}
