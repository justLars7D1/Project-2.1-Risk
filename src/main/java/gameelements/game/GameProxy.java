package gameelements.game;

import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.player.Player;

import java.util.LinkedList;

/**
 * Handles game events and processes the interaction between players
 */
public class GameProxy implements GameObserver {

    private Board gameBoard = new Board();

    public LinkedList<Player> players;

    public GameProxy(LinkedList<Player> players) {
        this.players = players;
    }

    @Override
    public void onGameEvent() {
        //TODO: battle and setup events during the gameplay
    }

    /**
     * gameelements.player accessible methods
     */
    protected void sendSoldiers(Country country) {
        //TODO: access the map and modify units in country
    }

    private Player getNextPlayer() {
        Player nextPlayer = players.peek();
        if (nextPlayer == null) nextPlayer = players.getFirst();
        return nextPlayer;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    @Override
    public String toString() {
        return "GameProxy{" +
                "players=" + players +
                '}';
    }
}
