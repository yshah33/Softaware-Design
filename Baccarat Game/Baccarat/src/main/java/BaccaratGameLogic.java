/**********************************************************
 * Program 2: Baccarat

 Course: CS 342, Fall 2023.
 Author: Yashwi S & Maria B

 Project Description: The class BaccaratGameLogic provides the logic for evaluating the
 game of Baccarat. It contains methods to determine the winner of the game, calculate the
 total points of a hand, and decide whether the banker or player should be dealt a third card.

 **********************************************************/
import java.util.ArrayList;


public class BaccaratGameLogic {
    /*
    The method whoWon will evaluate two hands at the end of the game and return a string
    depending on the winner: “Player”, “Banker”, “Draw”.
     */
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2) {
        int player = handTotal(hand1);
        int banker = handTotal(hand2);

        if (player > banker) {
            return "Player";
        }
        else if (banker > player) {
            return "Banker";
        }
            return "Draw";
    }

    /*
    The method handTotal will take a
    hand and return how many points that hand is worth.
     */
    public int handTotal(ArrayList<Card> hand) {
        int sum = 0;
        for (Card cardValue : hand) {
            sum = sum + cardValue.cardValue();
        }
        sum = sum % 10;

        return sum;
    }

    /*
    The methods evaluateBankerDraw
    and evaluatePlayerDraw will return true if either one should be dealt a third card,
    otherwise return false.
     */
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){

        if (handTotal(hand) >= 7) {
            return false;
        }
        else if (handTotal(hand) <= 2) {
            return true;
        }
        else if (handTotal(hand) == 3) {
            return (playerCard == null || playerCard.cardValue() != 8);
        }
        else if (handTotal(hand) == 4) {
            return (playerCard != null && (playerCard.cardValue() >= 2 && playerCard.cardValue() <= 7));
        }
        else if (handTotal(hand) == 5) {
            return (playerCard != null && (playerCard.cardValue() >= 4 && playerCard.cardValue() <= 7));
        }
        else if (handTotal(hand) == 6) {
            return (playerCard != null && (playerCard.cardValue() == 6 || playerCard.cardValue() == 7));
        }
        return false;
    }

    public boolean evaluatePlayerDraw(ArrayList<Card> hand) {
        if (handTotal(hand) < 6) {
            return true;
        }
        return false;
    }
}
