package gameelements.player;

import java.util.LinkedList;

public class PlayerList {

    private LinkedList<Player> players;
    Player currentPlayer;

    public PlayerList(LinkedList<Player> players) {
        this.players = players;
        getNextPlayer();
    }

    public Player getNextPlayer() {
        this.currentPlayer = players.poll();
        players.add(this.currentPlayer);
        return this.currentPlayer;
    }

    public void removePlayer(Player p) {
        players.remove(p);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

}
