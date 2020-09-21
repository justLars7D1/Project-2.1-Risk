package board;

import player.GameProxy;
import player.Player;
import player.PlayerFactory;

import java.util.*;

public class Game {

    private GameProxy proxy;

    public Game() {
    }

    public void buildSetup(HashMap<Integer, String> playerSelection) {
        LinkedList<Player> players = new LinkedList<>();

        List<Integer> playerIDs = Arrays.asList(playerSelection.keySet().toArray(new Integer[0]));
        Collections.shuffle(playerIDs);

        for (int id: playerIDs) {
            Player p;
            if (playerSelection.get(id).equalsIgnoreCase("user")) {
                // Add id to it
                p = PlayerFactory.createHumanPlayer();
            } else {
                // Add id to it
                p = PlayerFactory.createAIPlayer();
            }
            players.add(p);
        }

        proxy = new GameProxy(players);
    }

}
