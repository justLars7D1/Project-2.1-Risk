package gameelements.game;

import gameelements.board.Board;
import gameelements.phases.data.*;
import gameelements.phases.BattlePhase;
import gameelements.phases.GamePhase;
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

    /**
     * The list of players (acts as a cyclic linked list)
     */
    private PlayerList players;

    /**
     * Constructor
     * Sets selection of players and sets up starting game phases
     * @param playerSelection The player selection
     */
    public Game(HashMap<Integer, Integer> playerSelection) {
        super(GamePhase.DISTRIBUTION, BattlePhase.PLACEMENT);
        buildPlayerSetup(playerSelection);
    }

    @Override
    protected void onDistributionEvent(DistributionEventData data) {

    }

    @Override
    protected void onPlacementEvent(PlacementEventData data) {

    }

    @Override
    protected void onAttackEvent(AttackEventData data) {

    }

    @Override
    protected void onFortifyEvent(FortifyEventData data) {

    }

    /**
     * Creates the game from the hashmap configuration
     * Builds a random order for turns
     * @param playerSelection The hashmap containing gameelements.player configurations
     */
    private void buildPlayerSetup(HashMap<Integer, Integer> playerSelection) {
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

        this.players = new PlayerList(players);
    }

    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    public BattlePhase getBattlePhase() {
        return this.battlePhase;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public Player getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    @Override
    public String toString() {
        return "Game{" +
                "\n\tgamePhase = " + gamePhase +
                ",\n\tbattlePhase = " + battlePhase +
                "\n}";
    }

}
