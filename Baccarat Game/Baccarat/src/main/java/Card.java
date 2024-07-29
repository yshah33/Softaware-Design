/**********************************************************
 * Program 2: Baccarat

 Course: CS 342, Fall 2023.
 Author: Yashwi S & Maria B

 Project Description: The card class has information to a specific
 card in the deck (suite, value, card value). Setter and getter
 allow accessing and manipulating the values of the card. Face card
 has card value of 0, Aces has 1, and other has the value of card.
 **********************************************************/

public class Card {

    // Declared private variables
    private String suite;
    private int value;

    // set up getter for suite and value
    public String getSuite() {
        return suite;
    }

    public int getValue() {
        return value;
    }

    // Return card value (11, 12, 13 are face card -> return 0)
    public int cardValue () {
        if (value == 11 || value == 12 || value == 13) {
            return 0;
        }
        return value;
    }

    // set up setter for suite and value
    public void setSuite(String suite) {
        this.suite = suite;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // Fully parameterized constructor
    Card(String theSuite, int theValue) {
        this.suite = theSuite;
        this.value = theValue;
    }
}

