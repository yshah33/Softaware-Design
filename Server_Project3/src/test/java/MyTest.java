import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	// ****************** TESTING WordGenerator.JAVA ******************


	@Test
	public void initializeCategoriesTest() {
		WordGenerator wordGenerator = new WordGenerator();
		assertNotNull(wordGenerator.getCategories(), "Categories should not be null");
		assertEquals(3, wordGenerator.getCategories().size(), "There should be three categories initialized");
	}

	@Test
	public void initializeCategoriesContainsFruitsTest() {
		WordGenerator wordGenerator = new WordGenerator();
		assertTrue(wordGenerator.getCategories().stream().anyMatch(cat -> "FRUITS".equals(cat.getCategoryName())), "Fruits category should be present");
	}

	@Test
	public void initializeCategoriesContainsAnimalsTest() {
		WordGenerator wordGenerator = new WordGenerator();
		assertTrue(wordGenerator.getCategories().stream().anyMatch(cat -> "ANIMALS".equals(cat.getCategoryName())), "Animals category should be present");
	}

	@Test
	public void getRandomWordFromAnimalsCategoryTest() {
		WordGenerator wordGenerator = new WordGenerator();
		Category animalCategory = wordGenerator.getCategories().stream()
				.filter(cat -> "ANIMALS".equals(cat.getCategoryName()))
				.findFirst()
				.orElse(null);
		assertNotNull(animalCategory, "Animals category should exist");
		String randomWord = wordGenerator.getRandomWord(animalCategory);
		assertTrue(animalCategory.getWordList().contains(randomWord), "Random word should be from the Animals category");
	}

	@Test
	public void getRandomWordFromFruitsCategoryTest() {
		WordGenerator wordGenerator = new WordGenerator();
		Category fruitCategory = wordGenerator.getCategories().stream()
				.filter(cat -> "FRUITS".equals(cat.getCategoryName()))
				.findFirst()
				.orElse(null);
		assertNotNull(fruitCategory, "Fruits category should exist");
		String randomWord = wordGenerator.getRandomWord(fruitCategory);
		assertTrue(fruitCategory.getWordList().contains(randomWord), "Random word should be from the Fruits category");
	}

	@Test
	public void getRandomWordFromCountriesCategoryTest() {
		WordGenerator wordGenerator = new WordGenerator();
		Category countryCategory = wordGenerator.getCategories().stream()
				.filter(cat -> "COUNTRIES".equals(cat.getCategoryName()))
				.findFirst()
				.orElse(null);
		assertNotNull(countryCategory, "Countries category should exist");
		String randomWord = wordGenerator.getRandomWord(countryCategory);
		assertTrue(countryCategory.getWordList().contains(randomWord), "Random word should be from the Countries category");
	}


	// ****************** TESTING GameLogic.JAVA ******************
	@Test
	public void validateGuessCorrectGuessTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.remainingGuesses = 5;
		game.guess = 'a';
		int remaining = logic.validateGuess("apple", game);
		assertEquals(5, remaining, "Remaining guesses should not decrease on correct guess");
	}

	@Test
	public void validateGuessIncorrectGuessTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.remainingGuesses = 5;
		game.guess = 'z';
		int remaining = logic.validateGuess("apple", game);
		assertEquals(4, remaining, "Remaining guesses should decrease by 1 on incorrect guess");
	}

	@Test
	public void validateGuessNoGuessesLeftTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.remainingGuesses = 0;
		game.guess = 'z';
		int remaining = logic.validateGuess("apple", game);
		assertEquals(0, remaining, "Remaining guesses should remain 0 if no guesses left");
	}

	@Test
	public void validateUserGuessCorrectTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guess = 'a';
		assertTrue(logic.validateUserGuess("apple", game), "Should return true for correct guess");
	}

	@Test
	public void validateUserGuessIncorrectTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guess = 'z';
		assertFalse(logic.validateUserGuess("apple", game), "Should return false for incorrect guess");
	}

	@Test
	public void validateUserGuessEmptyWordTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guess = 'a';
		assertFalse(logic.validateUserGuess("", game), "Should return false for empty word");
	}

	@Test
	public void isWordGuessedAllLettersGuessedTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guessedLetters.addAll(List.of('a', 'p', 'l', 'e'));
		assertTrue(logic.isWordGuessed(game, "apple"), "Should return true if all letters are guessed");
	}

	@Test
	public void isWordGuessedSomeLettersGuessedTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guessedLetters.addAll(List.of('a', 'p'));
		assertFalse(logic.isWordGuessed(game, "apple"), "Should return false if some letters are not guessed");
	}

	@Test
	public void isWordGuessedNoLettersGuessedTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		assertFalse(logic.isWordGuessed(game, "apple"), "Should return false if no letters are guessed");
	}

	@Test
	public void wordLengthTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		assertEquals(5, logic.wordLength("apple", game), "Length of 'apple' should be 5");
	}

	@Test
	public void wordLengthEmptyStringTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		assertEquals(0, logic.wordLength("", game), "Length of an empty string should be 0");
	}

	@Test
	public void wordLengthSingleCharacterTest() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		assertEquals(1, logic.wordLength("a", game), "Length of 'a' should be 1");
	}

	@Test
	public void positionTestSingleOccurrence() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guess = 'a';
		List<Integer> positions = logic.position("apple", game);
		assertEquals(List.of(0), positions, "Position of 'a' in 'apple' should be [0]");
	}

	@Test
	public void restartGameShouldResetCategoryName() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.category.setCategoryName("FRUITS"); // Set initial state
		logic.restartGame(game); // Reset the game
		assertEquals("", game.category.getCategoryName(), "Category name should be reset to empty string");
	}

	@Test
	public void restartGameShouldResetGuess() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guess = 'a'; // Set initial state
		logic.restartGame(game); // Reset the game
		assertEquals(0, game.guess, "Guess should be reset to 0");
	}

	@Test
	public void restartGameShouldClearGuessedLetters() {
		GameLogic logic = new GameLogic();
		Game game = new Game();
		game.guessedLetters.add('a'); // Set initial state
		game.guessedLetters.add('e'); // Set initial state
		logic.restartGame(game); // Reset the game
		assertTrue(game.guessedLetters.isEmpty(), "Guessed letters should be cleared");
	}

	// ****************** TESTING Game.JAVA ******************

	//startNewGame method
	@Test
	public void startNewGameShouldResetFields() {
		Game game = new Game();
		game.remainingGuesses = 0;
		game.guessedLetters.add('a');
		game.position.add(1);
		game.numberOfLetter = 5;
		game.isGameWon = true;
		game.isGameLoss = true;
		game.isReplay = true;
		game.isGameOver = true;

		game.startNewGame();

		assertEquals(6, game.remainingGuesses, "Remaining guesses should be reset to 6");
		assertTrue(game.guessedLetters.isEmpty(), "Guessed letters should be cleared");
		assertTrue(game.position.isEmpty(), "Position list should be cleared");
		assertEquals(0, game.numberOfLetter, "Number of letters should be reset to 0");
		assertFalse(game.isGameWon, "isGameWon should be reset to false");
		assertFalse(game.isGameLoss, "isGameLoss should be reset to false");
		assertFalse(game.isReplay, "isReplay should be reset to false");
		assertFalse(game.isGameOver, "isGameOver should be reset to false");
	}

	//Try again method
	@Test
	public void tryAgainGameShouldResetFields() {
		Game game = new Game();
		game.remainingGuesses = 0;
		game.guessedLetters.add('a');
		game.position.add(1);
		game.isGameWon = true;
		game.isGameLoss = true;
		game.isReplay = true;
		game.isGameOver = true;

		game.tryAgainGame();

		assertEquals(6, game.remainingGuesses, "Remaining guesses should be reset to 6");
		assertTrue(game.guessedLetters.isEmpty(), "Guessed letters should be cleared");
		assertTrue(game.position.isEmpty(), "Position list should be cleared");
		assertFalse(game.isGameWon, "isGameWon should be reset to false");
		assertFalse(game.isGameLoss, "isGameLoss should be reset to false");
		assertFalse(game.isReplay, "isReplay should be reset to false");
		assertFalse(game.isGameOver, "isGameOver should be reset to false");
	}

}
