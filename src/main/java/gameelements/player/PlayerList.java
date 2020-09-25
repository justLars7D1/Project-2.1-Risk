package gameelements.player;

import java.util.Queue;

public class PlayerList {

    private Queue<Player> players;
    private Player currentPlayer;

    public PlayerList(Queue<Player> players) {
        this.players = players;
        getNextPlayer();
    }

    public Player getNextPlayer() {
        Player nextPlayer = players.poll();
        players.add(nextPlayer);
        currentPlayer = nextPlayer;
        return nextPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

}
