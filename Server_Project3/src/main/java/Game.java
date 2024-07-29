import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    public Category category;
    public int numberOfLetter;
    public List<Character> guessedLetters;
    public int remainingGuesses;
    public char guess;
    public boolean isValidGuess;
    public boolean isGameWon;
    public boolean isGameLoss;
    public boolean isGameOver;
    public boolean isReplay;
    public String flag;
    public List<Integer> position;
    int animaAttempt = 3;
    int fruitsAttempt = 3;
    int countryAttempt = 3;

    // constructor
    public Game(Category category) {
        this.category = category;
        this.guessedLetters = new ArrayList<>();
        this.position = new ArrayList<>();
        this.startNewGame();
    }
    public Game(){
        this.guessedLetters = new ArrayList<>();
        this.position = new ArrayList<>();
        this.startNewGame();
    }

    // setup everything to initialize values and start the new game
    public void startNewGame() {
        System.out.println("i am in the start now");
        this.remainingGuesses = 6;
        this.guessedLetters.clear();
        this.position.clear();
        this.numberOfLetter = 0;
        this.isGameWon = false;
        this.isGameLoss = false;
        this.isReplay = false;
        this.isGameOver = false;
    }

    public void tryAgainGame(){
        this.remainingGuesses = 6;
        this.isGameWon = false;
        this.isGameLoss = false;
        this.isReplay = false;
        this.position.clear();
        this.guessedLetters.clear();
        this.isGameOver = false;
    }

}

