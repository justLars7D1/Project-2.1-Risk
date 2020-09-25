package gameelements.game;

import gameelements.board.Board;
import gameelements.data.BattleEventData;
import gameelements.data.DistributionEventData;
import gameelements.data.GameEventData;
import gameelements.player.Player;
import gameelements.player.PlayerFactory;
import gameelements.player.PlayerList;

import java.util.*;

/**
 * Represents the implementation of the back-end of the game
 */
public class Game extends GameObserver {

    /**
     * Handles game events and processes the interaction between players
     */
    private Board gameBoard = new Board();

    private PlayerList players;

    public Game(HashMap<Integer, Integer> playerSelection) {
        super(GamePhase.DISTRIBUTION, GamePhase.BATTLE.PLACEMENT);
        buildPlayerSetup(playerSelection);
    }

    protected void onDistributionEvent(DistributionEventData data) {
    }

    protected void onBattleEvent(BattleEventData data) {
    }

    /**
     * Creates the game from the hashmap configuration
     * Builds a random order for turns
     * @param playerSelection The hashmap containing gameelements.player configurations
     */
    private void buildPlayerSetup(HashMap<Integer, Integer> playerSelection) {
        Queue<Player> players = new LinkedList<>();

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

        this.players = new PlayerList(players);
    }

    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    public GamePhase.BATTLE getBattlePhase() {
        return this.battlePhase;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    @Override
    public String toString() {
        return "Game{" +
                ",\n\tgamePhase = " + gamePhase +
                ",\n\tbattlePhase = " + battlePhase +
                "\n}";
    }

}
