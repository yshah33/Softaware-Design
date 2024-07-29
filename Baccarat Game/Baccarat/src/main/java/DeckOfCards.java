/**********************************************************
 * Program 2: Baccarat

 Course: CS 342, Fall 2023.
 Author: Yashwi S & Maria B

 Project Description: The DeckOfCards create standard deck of playing cards,
 initializing a collection containing all 52 cards with four suites and values
 ranging from Ace to King.
 **********************************************************/
import java.util.ArrayList;

public class DeckOfCards {

    // Declared public variables for deck
    public ArrayList<Card> cards;
    public static final String[] SUITES = {"S", "H", "D", "C"};

    // 11 for Jack, 12 for Queen, 13 for King, 1 for Ace
    public static final int[] VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    //Default constructor, set up the cards deck
    public DeckOfCards() {
        this.cards = new ArrayList<Card>();

        for (String suite : SUITES) {
            for (int value : VALUES) {
                Card card = new Card(suite, value);
                this.cards.add(card);
            }
        }
    }

    // set  up getter and setter for cards arrayList
    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

}
