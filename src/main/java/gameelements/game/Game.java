package gameelements.game;

import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.phases.data.*;
import gameelements.phases.BattlePhase;
import gameelements.phases.GamePhase;
import gameelements.player.Player;
import gameelements.player.PlayerFactory;
import gameelements.player.PlayerList;
import settings.Settings;

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
        Player player = getCurrentPlayer();
        Country country = gameBoard.getCountryFromID(data.getCountryID());

        player.onDistributionEvent(country);

        players.nextPlayer();
        checkDistributionPhase();
    }

    private void checkDistributionPhase() {
        List<Player> players = this.players.getPlayers();
        boolean isDistributionPhaseOver = true;
        for (Player p: players) {
            if (p.getNumTroopsInInventory() > 0) {
                isDistributionPhaseOver = false;
                break;
            }
        }
        if (isDistributionPhaseOver) {
            for (Player p: players) calcNumNewPlayerTroops(p);
            gamePhase = GamePhase.BATTLE;
        }
    }

    @Override
    protected void onPlacementEvent(PlacementEventData data) {
        Player player = getCurrentPlayer();
        Country country = gameBoard.getCountryFromID(data.getCountryID());

        player.onPlacementEvent(country, data.getNumTroops());

    }

    @Override
    protected void onAttackEvent(AttackEventData data) {
        Player player = getCurrentPlayer();

        //TODO: Someone make this event
        player.onAttackEvent(data);

        // Check for the end of the game (so one player owns all countries)
        checkForGameEnd();
    }

    @Override
    protected void onFortifyEvent(FortifyEventData data) {
        Player player = getCurrentPlayer();
        Country countryFrom = gameBoard.getCountryFromID(data.getFromCountryID());
        Country countryTo = gameBoard.getCountryFromID(data.getToCountryID());

        player.onFortifyEvent(countryFrom, countryTo, data.getNumTroops());

    }

    private void checkForGameEnd() {
        boolean gameOver = false;
        for (Player p: players.getPlayers()) {
            if (p.getNumCountriesOwned() == Settings.countries.length) {
                gameOver = true;
                break;
            }
        }

        if (gameOver) {
            gamePhase = GamePhase.VICTORY;
        }
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

        int numTroopsInInventory = getNumStartingTroops(playerIDs.size());

        for (int id: playerIDs) {
            Player p;
            // 1 == user
            if (playerSelection.get(id) == 1) {
                p = PlayerFactory.createHumanPlayer(id, numTroopsInInventory);
            } else {
                p = PlayerFactory.createAIPlayer(id, numTroopsInInventory);
            }
            players.add(p);
        }

        this.players = new PlayerList(players);
    }

    private int getNumStartingTroops(int numPlayers) {
        int numCardsPerPlayer;
        switch (numPlayers) {
            case 3:
                numCardsPerPlayer = 35;
                break;
            case 4:
                numCardsPerPlayer = 30;
                break;
            case 5:
                numCardsPerPlayer = 25;
                break;
            case 6:
                numCardsPerPlayer = 20;
                break;
            default:
                numCardsPerPlayer = 40;
                break;
        }
        return numCardsPerPlayer;
    }

    /**
     * Continues to the next phase in attacking (or goes to next player if it's over)
     * Placement -> Attack -> Fortify -> New Player -> Placement -> ...
     */
    public void finishAttackPhase() {
        switch (battlePhase) {
            case PLACEMENT:
                battlePhase = BattlePhase.ATTACK;
                break;
            case ATTACK:
                removePlayersWithoutTerritory();
                battlePhase = BattlePhase.FORTIFYING;
                break;
            case FORTIFYING:
                calcNumNewPlayerTroops(getCurrentPlayer());
                players.nextPlayer();
                battlePhase = BattlePhase.PLACEMENT;
                break;
        }
    }

    private void removePlayersWithoutTerritory() {
        for (Player p: players.getPlayers()) {
            if (p.getNumCountriesOwned() == 0) {
                players.removePlayer(p);
            }
        }
    }

    private void calcNumNewPlayerTroops(Player p) {
        int totalNewTroops = 0;
        // Number of troops for territory owned with a minimum number of troops of 3
        totalNewTroops += Math.max(
                Settings.MINNUMTROOPSPERTURN,
                p.getNumCountriesOwned() / Settings.TROOPSPERCOUNTRYDIVIDER
        );
        // TODO: Number of troops for continent
        // TODO: Number of troops for cards
        p.setNumTroopsInInventory(totalNewTroops);
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
