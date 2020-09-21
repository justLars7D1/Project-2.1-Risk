package board;

import player.GameProxy;
import player.Player;
import player.PlayerFactory;

import java.util.*;

/**
 * Represents the implementation of the back-end of the game
 */
public class Game implements GameInterface {

    /**
     * Handles game events and processes the interaction between players
     */
    private GameProxy proxy;

    public Game() {
    }

    /**
     * Creates the game from the hashmap configuration
     * Builds a random order for turns
     * @param playerSelection The hashmap containing player configurations
     */
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

    @Override
    public void startPlacementPhase() {

    }

    @Override
    public void startBattlePhase() {

    }
}
