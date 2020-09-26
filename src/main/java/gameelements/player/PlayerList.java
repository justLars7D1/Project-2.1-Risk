package gameelements.player;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a cyclic iterative of players
 */
public class PlayerList {

    /**
     * The player list
     */
    private LinkedList<Player> players;
    Player currentPlayer;

    public PlayerList(LinkedList<Player> players) {
        this.players = players;
        nextPlayer();
    }

    /**
     * Goes to the next player in the list
     */
    public void nextPlayer() {
        this.currentPlayer = players.poll();
        players.add(this.currentPlayer);
    }

    /**
     * Removed a player from the list
     * @param p player to be removed
     */
    public void removePlayer(Player p) {
        players.remove(p);
    }

    /**
     * Gets the current player
     * @return Current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets all players
     * @return The players in the game
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

}
