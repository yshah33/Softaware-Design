import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

class MyTest {
	// NEED CONSTRUCTOR FOR ALL CLASS

	// ****************** TESTING CARD.JAVA ******************
	private Card card;						// Declare the private variable

	@BeforeEach
	public void setUp() {
		card = new Card("H", 10);
	}

	// Test getter for suite
	@Test
	public void GetSuiteTest1 () {
		assertEquals("H", card.getSuite(), "TEST 1 FAIL: wrong value");
	}

	@Test
	public void GetSuiteTest2 () {
		card.setSuite("D");
		assertEquals("D", card.getSuite(), "TEST 2 FAIL: wrong value");
	}

	// Test getter for value
	@Test
	public void GetValueTest1 () {
		assertEquals(10, card.getValue(), "TEST 3 FAIL: wrong value");
	}

	@Test
	public void GetValueTest2() {
		card.setValue(5);
		assertEquals(5, card.getValue(), "TEST 4 FAIL: wrong value");
	}

	@Test
	public void CardValueForNormalValuesTest1 () {
		assertEquals(10, card.cardValue(), "TEST 5 FAIL: wrong value");
	}

	@Test
	public void CardValueForNormalValuesTest2 () {
		card.setValue(7);
		assertEquals(7, card.cardValue(), "TEST 6 FAIL: wrong value");
	}

	// Test card value is right for special values (face cards)
	@Test
	public void CardValueForSpecialValuesTest1 () {
		card.setValue(11);
		assertEquals(0, card.cardValue(), "TEST 7 FAIL: wrong value");
	}

	@Test
	public void CardValueForSpecialValuesSecondTest2 () {
		card.setValue(12);
		assertEquals(0, card.cardValue(), "TEST 8 FAIL: wrong value");
	}

	// Test setter for suite
	@Test
	public void SetSuiteTest1 () {
		card.setSuite("D");
		assertEquals("D", card.getSuite(), "TEST 9 FAIL: wrong value");
	}

	@Test
	public void SetSuiteTest2 () {
		card.setSuite("S");
		assertEquals("S", card.getSuite(), "TEST 10 FAIL: wrong value");
	}

	// Test setter for value
	@Test
	public void SetValueTest1 () {
		card.setValue(5);
		assertEquals(5, card.getValue(), "TEST 11 FAIL: wrong value");
	}

	@Test
	public void SetValueTest2 () {
		card.setValue(7);
		assertEquals(7, card.getValue(), "TEST 12 FAIL: wrong value");
	}

	// ****************** TESTING BaccaratGame.JAVA ******************

	// Test total winning calculates expected amount for banker bet
	@Test
	void evaluateWinningsTest1() {
		BaccaratGame game = new BaccaratGame();
		game.playerHand = new ArrayList<>();
		game.bankerHand = new ArrayList<>();

		// Set up a scenario for player and banker hand
		game.playerHand.add(new Card("S", 6));
		game.playerHand.add(new Card("S", 8));
		game.bankerHand.add(new Card("D", 3));
		game.bankerHand.add(new Card("D", 4));

		// set up bet and amount
		game.setUserBet("Banker");
		game.setCurrentBet(100);

		assertEquals(95, game.evaluateWinnings(), "TEST 13 FAIL: wrong value");
	}

	// Test total winning calculates expected amount for player bet
	@Test
	void evaluateWinningsTest2() {
		BaccaratGame game = new BaccaratGame();
		game.playerHand = new ArrayList<>();
		game.bankerHand = new ArrayList<>();

		// Set up a scenario for player and banker hand
		game.playerHand.add(new Card("S", 1));
		game.playerHand.add(new Card("S", 6));
		game.bankerHand.add(new Card("D", 9));
		game.bankerHand.add(new Card("D", 10));

		// set up bet and amount
		game.setUserBet("Player");
		game.setCurrentBet(50);

		assertEquals(-50, game.evaluateWinnings(), "TEST 14 FAIL: wrong value");
	}

	// Test total winning calculates expected amount for bet on draw
	@Test
	void evaluateWinningsTest3() {
		BaccaratGame game = new BaccaratGame();
		game.playerHand = new ArrayList<>();
		game.bankerHand = new ArrayList<>();

		// Set up a scenario for player and banker hand
		game.playerHand.add(new Card("S", 1));
		game.playerHand.add(new Card("S", 3));
		game.playerHand.add(new Card("D", 5));
		game.bankerHand.add(new Card("D", 7));
		game.bankerHand.add(new Card("D", 2));

		// set up bet and amount
		game.setUserBet("Draw");
		game.setCurrentBet(10);

		assertEquals(80, game.evaluateWinnings(), "TEST 15 FAIL: wrong value");
	}

	// ****************** TESTING DeckOfCards.JAVA ******************
	public DeckOfCards deck;				// declared the public variables

	@BeforeEach
	public void setUpDeck() {
		deck = new DeckOfCards();
	}

	// Test the deck if valid
	@Test
	public void InitialDeckTest1 () {
		ArrayList<Card> cards = deck.getCards();

		// Test that the deck has 52 cards
		assertEquals(52, cards.size(), "TEST 16 FAIL: wrong value");

		// Test that each suite has 13 cards
		int[] countBySuite = new int[4];
		for (Card card : cards) {
			switch (card.getSuite()) {
				case "S":
					countBySuite[0]++;
					break;
				case "H":
					countBySuite[1]++;
					break;
				case "D":
					countBySuite[2]++;
					break;
				case "C":
					countBySuite[3]++;
					break;
			}
		}

		// Count each suits contains 13 cards total
		for (int count : countBySuite) {
			assertEquals(13, count, "TEST 16 FAIL: wrong value");
		}

		// calling the functions
		CheckEachSuiteValueTest();
		DeckSize();
	}

	// Test is suite contains valid values
	public void CheckEachSuiteValueTest() {
		for (String suite : DeckOfCards.SUITES) {
			for (int value : DeckOfCards.VALUES) {
				assertTrue(deck.getCards().stream().anyMatch(card -> card.getSuite().equals(suite) && card.getValue() == value), "TEST 16 FAIL: wrong value");
			}
		}
	}

	// Test deck size (contains 52 cards total)
	public void DeckSize() {
		Set<Card> uniqueCards = new HashSet<>(deck.getCards());
		assertEquals(52, uniqueCards.size(), "TEST 16 FAIL: wrong value");
	}

	// ****************** TESTING BaccaratGameLogic.JAVA ******************
	@Test
	public void WhoWonTest1 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> playerHand = new ArrayList<>();
		ArrayList<Card> bankerHand = new ArrayList<>();

		// Set up a scenario for player and banker hand
		playerHand.add(new Card("H", 5));
		playerHand.add(new Card("H", 7));
		bankerHand.add(new Card("S", 3));
		bankerHand.add(new Card("C", 6));

		// Test is need to draw another card for player
		assertTrue(gameLogic.evaluatePlayerDraw(playerHand), "TEST 17 FAIL: wrong value");
		playerHand.add(new Card("H", 2));

		// Test is need to draw another card for banker
		Card AnotherCard = new Card("H", 5);
		assertFalse(gameLogic.evaluateBankerDraw(bankerHand, AnotherCard), "TEST 17 FAIL: wrong value");

		String won = gameLogic.whoWon(playerHand, bankerHand);		// store who won
		assertEquals("Banker", won, "TEST 17 FAIL: wrong value");
	}

	@Test
	public void WhoWonTest2 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> playerHand = new ArrayList<>();
		ArrayList<Card> bankerHand = new ArrayList<>();

		// Set up a scenario for player and banker hand
		playerHand.add(new Card("H", 7));
		playerHand.add(new Card("H", 2));
		bankerHand.add(new Card("S", 2));
		bankerHand.add(new Card("C", 6));

		String won = gameLogic.whoWon(playerHand, bankerHand);			// store who won
		assertEquals("Player", won, "TEST 18 FAIL: wrong value");
	}

	@Test
	public void WhoWonTest3 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> playerHand = new ArrayList<>();
		ArrayList<Card> bankerHand = new ArrayList<>();

		// Set up a scenario for player and banker hand
		playerHand.add(new Card("H", 3));
		playerHand.add(new Card("H", 2));
		playerHand.add(new Card("D", 4));
		bankerHand.add(new Card("S", 6));
		bankerHand.add(new Card("C", 3));

		String won = gameLogic.whoWon(playerHand, bankerHand);			// store who won
		assertEquals("Draw", won, "TEST 19 FAIL: wrong value");
	}

	@Test
	public void handTotalTest1() {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> hand = new ArrayList<>();

		// Set up a scenario in hand
		hand.add(new Card("H", 5));
		hand.add(new Card("H", 7));

		// Calculate the total value of the hand
		int total = gameLogic.handTotal(hand);
		assertEquals(2, total, "TEST 20 FAIL: wrong value");
	}

	@Test
	public void handTotalTest2() {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> hand = new ArrayList<>();

		// Set up a scenario in hand
		hand.add(new Card("H", 8));
		hand.add(new Card("S", 4));

		// Calculate the total value of the hand
		int total = gameLogic.handTotal(hand);
		assertEquals(2, total, "TEST 21 FAIL: wrong value");
	}

	@Test
	public void evaluateBankerDrawTest1 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> bankerHand = new ArrayList<>();

		// Set up a scenario in hand
		bankerHand.add(new Card("S", 2));
		bankerHand.add(new Card("C", 3));

		// Check if the banker should draw a third card
		boolean shouldDraw = gameLogic.evaluateBankerDraw(bankerHand, new Card("H", 5));
		assertTrue(shouldDraw, "TEST 22 FAIL: wrong value");
	}

	@Test
	public void evaluateBankerDrawTest2 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> bankerHand = new ArrayList<>();

		// Set up a scenario in hand
		bankerHand.add(new Card("S", 4));
		bankerHand.add(new Card("C", 3));

		Card newCard = new Card("H", 5);
		// Check if the banker should draw a third card
		boolean shouldDraw = gameLogic.evaluateBankerDraw(bankerHand, newCard);
		assertFalse(shouldDraw, "TEST 23 FAIL: wrong value");
	}

	@Test
	public void evaluatePlayerDrawTest1 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> playerHand = new ArrayList<>();

		// Set up a scenario in hand
		playerHand.add(new Card("H", 2));
		playerHand.add(new Card("S", 2));

		// Check if the player should draw a third card
		boolean shouldDraw = gameLogic.evaluatePlayerDraw(playerHand);
		assertTrue(shouldDraw, "TEST 24 FAIL: wrong value");
	}

	@Test
	public void evaluatePlayerDrawTest2 () {
		BaccaratGameLogic gameLogic = new BaccaratGameLogic();
		ArrayList<Card> playerHand = new ArrayList<>();

		// Set up a scenario in hand
		playerHand.add(new Card("H", 6));
		playerHand.add(new Card("S", 2));

		// Check if the player should draw a third card
		boolean shouldDraw = gameLogic.evaluatePlayerDraw(playerHand);
		assertFalse(shouldDraw, "TEST 25 FAIL: wrong value");
	}

}

