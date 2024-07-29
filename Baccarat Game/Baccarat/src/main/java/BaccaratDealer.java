/**********************************************************
 * Program 2: Baccarat

 Course: CS 342, Fall 2023.
 Author: Yashwi S & Maria B

 Project Description: The BaccaratDealer class handles the dealer responsibilities
 of managing and manipulating the deck of cards. It generates a deck of cards and
 shuffles when drawing or needed. It deals with a hand of cards, drawing individual
 cards from the deck, and tracking the deck's size.
 **********************************************************/
import java.util.ArrayList;
import java.util.Collections;


public class BaccaratDealer {
    private ArrayList<Card> deck;               // declared private variable

    // default constructor
    public BaccaratDealer(){
        generateDeck();
        shuffleDeck();
    }

    // set up setter and getter for deck arrayList
    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void generateDeck() {
        DeckOfCards deckOfCards = new DeckOfCards();
        this.deck = deckOfCards.getCards();
    }

    // Creates a new hand and add two cards from the deck
    // Return the hand containing the two drawn cards.
    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(drawOne());
        hand.add(drawOne());
        return hand;
    }

    // Draws a single card from the deck.
    // Return the card drawn from the deck, or null if the deck is empty.
    public Card drawOne() {

        // shuffle the deck and remove that card from the deck
        if (deckSize() == 0 || deckSize() < 6) {
            shuffleDeck();
        }
        if(deckSize() > 0){
            return deck.remove(0);
        }

        return null;                            // If the deck is empty, return null
    }

    // Generate a new deck of cards
    // Shuffles the deck using the Collections.shuffle()
    public void shuffleDeck() {
        generateDeck();
        Collections.shuffle(deck);
    }

    // Return the deck size
    public int deckSize() {
        return deck.size();
    }

}