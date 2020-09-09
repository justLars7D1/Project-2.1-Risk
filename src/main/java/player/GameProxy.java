package player;

import java.util.LinkedList;

/**
 * Handles game events and processes the interaction between players
 */
public class GameProxy implements GameObserver {
    LinkedList<Player> players;

    public GameProxy(LinkedList<Player> players){
        this.players = players;
    }

    @Override
    public void onGameEvent(){
        //TODO: battle and setup events during the gameplay
    }
}
