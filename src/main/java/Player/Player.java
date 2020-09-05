package Player;
import Graph.Country;

import java.util.*;

public abstract class Player {
    private int[] cards = new int[4];
    private TreeSet<Country> countriesOwned;
    private final int CARDSALLOWED;

    protected Player(int cardsallowed) {
        CARDSALLOWED = cardsallowed;
    }

    private List<Integer> rollDice(int dice){
        Random die = new Random();
        List<Integer> diceResults = new ArrayList<>();
        for(int i = 0; i < dice; i++){
            int d = die.nextInt(5);
            diceResults.add(d);
        }
        return diceResults;
    }
    private boolean fullHand(){
        int sum = 0;
        for(int i=0; i < cards.length; i++){
            sum += cards[i];
        }
         return sum>=CARDSALLOWED;
        }
    private void endTurn(){
    }
    private void nextPhase(){}

}
