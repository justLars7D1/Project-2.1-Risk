package gameelements.game;

import gameelements.player.Player;
import gameelements.player.PlayerFactory;

import java.util.*;

/**
 * Represents the implementation of the back-end of the game
 */
public class Game {

    /**
     * Handles game events and processes the interaction between players
     */
    protected GameProxy proxy;

    private GamePhase gamePhase;

    private GamePhase.BATTLE battlePhase;

    public Game() {
        this.gamePhase = GamePhase.DISTRIBUTION;
        this.battlePhase = GamePhase.BATTLE.PLACEMENT;
    }

    /**
     * Creates the game from the hashmap configuration
     * Builds a random order for turns
     * @param playerSelection The hashmap containing gameelements.player configurations
     */
    public void buildSetup(HashMap<Integer, Integer> playerSelection) {
        LinkedList<Player> players = new LinkedList<>();

        List<Integer> playerIDs = Arrays.asList(playerSelection.keySet().toArray(new Integer[0]));
        Collections.shuffle(playerIDs);

        for (int id: playerIDs) {
            Player p;
            // 1 == user
            if (playerSelection.get(id) == 1) {
                p = PlayerFactory.createHumanPlayer(id);
            } else {
                p = PlayerFactory.createAIPlayer(id);
            }
            players.add(p);
        }

        proxy = new GameProxy(players);
    }

    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    public GamePhase.BATTLE getBattlePhase() {
        return this.battlePhase;
    }

    @Override
    public String toString() {
        return "Game{" +
                "\n\tproxy = " + proxy +
                ",\n\tgamePhase = " + gamePhase +
                ",\n\tbattlePhase = " + battlePhase +
                "\n}";
    }
}
