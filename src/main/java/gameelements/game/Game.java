package gameelements.game;

import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.phases.data.*;
import gameelements.phases.BattlePhase;
import gameelements.phases.GamePhase;
import gameelements.player.*;
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
     * The initial number of players in the game
     */
    private int numPlayers;

    private List<Country> conqueredCountries;

    /**
     * Boolean for using a fixed seed
     */
    private boolean useSeed = false;

    /**
     * The main seed of the game
     */
    private final Random random;

    /**
     * Constructor
     * Sets selection of players and sets up starting game phases
     * @param playerSelection The player selection
     */
    public Game(HashMap<Integer, PlayerType> playerSelection, int seed) {
        super(GamePhase.DISTRIBUTION, BattlePhase.PLACEMENT);
        this.random = new Random(seed);
        this.numPlayers = playerSelection.size();
        buildPlayerSetup(playerSelection);
    }

    public Game(HashMap<Integer, PlayerType> playerSelection) {
        super(GamePhase.DISTRIBUTION, BattlePhase.PLACEMENT);
        this.numPlayers = playerSelection.size();
        this.random = new Random();
        buildPlayerSetup(playerSelection);
    }

    @Override
    protected void onDistributionEvent(DistributionEventData data) {
        Player player = getCurrentPlayer();
        Country country = getCountryFromID(data.getCountryID());
        boolean isBoardFilled = gameBoard.isFilled();

        if (isBoardFilled || country == null || !country.hasOwner()) {
            boolean success = player.onDistributionEvent(country);
            if (success) players.nextPlayer();
        }

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

        Country country = getCountryFromID(data.getCountryID());

        player.onPlacementEvent(country, data.getNumTroops());
    }

    @Override
    protected void onAttackEvent(AttackEventData data) {
        Player player = getCurrentPlayer();
        Country countryFrom = getCountryFromID(data.getFromCountry());
        Country countryTo = getCountryFromID(data.getToCountry());

        player.onAttackEvent(countryFrom,countryTo);
        // Check for the end of the game (so one player owns all countries)
        checkForGameEnd();
    }

    @Override
    protected void onFortifyEvent(FortifyEventData data) {
        Player player = getCurrentPlayer();
        Country countryFrom = getCountryFromID(data.getFromCountryID());
        Country countryTo = getCountryFromID(data.getToCountryID());

        player.onFortifyEvent(countryFrom, countryTo, data.getNumTroops());
    }

    /**
     * The country ID equals -1 iff the bot is playing
     * @param countryID The id of the country
     * @return The country
     */
    private Country getCountryFromID(int countryID) {
        if (countryID == -1) {
            return null;
        } else {
            return gameBoard.getCountryFromID(countryID);
        }
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
    private void buildPlayerSetup(HashMap<Integer, PlayerType> playerSelection) {
        LinkedList<Player> players = new LinkedList<>();

        List<Integer> playerIDs = Arrays.asList(playerSelection.keySet().toArray(new Integer[0]));
        Collections.shuffle(playerIDs, random);

        int numTroopsInInventory = getNumStartingTroops(playerIDs.size());

        // If we're playing with a bot, enable that every attack is either a win or lose
        for (PlayerType playerType: playerSelection.values()) {
            if (playerType != PlayerType.USER) {
                Settings.ATTACKUNTILWINORLOSE = true;
                break;
            }
        }

        for (int id: playerIDs) {
            players.add(PlayerFactory.createPlayer(id, numTroopsInInventory, this, playerSelection.get(id)));
        }

        this.players = new PlayerList(players);

        if (useSeed) {
            for (Player player : this.players.getPlayers()) {
                player.setSeed(random.nextInt());
            }
        }
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
    public void nextBattlePhase() {
        switch (battlePhase) {
            case PLACEMENT:
                battlePhase = BattlePhase.ATTACK;
                // Initialize list
                this.conqueredCountries = new ArrayList<>();
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
        // Number of troops for owning continents
        totalNewTroops += numTroopsForContinents(p);
        p.setNumTroopsInInventory(totalNewTroops);
    }

    /** class needed for game feature  equal to calcNumNewPlayerTroops */
    public int getNumReinforcement(Player p){
        int totalNewTroops = 0;
        // Number of troops for territory owned with a minimum number of troops of 3
        totalNewTroops += Math.max(
                Settings.MINNUMTROOPSPERTURN,
                p.getNumCountriesOwned() / Settings.TROOPSPERCOUNTRYDIVIDER
        );
        // Number of troops for owning continents
        totalNewTroops += numTroopsForContinents(p);
        return totalNewTroops;
    }

    private int numTroopsForContinents(Player p) {
        HashSet<Country> playerCountries = p.getCountriesOwned();
        int numTroops = 0;
        // Iterate over all continents
        continentLoop: for (Map.Entry<String, Integer> continent: Settings.continentIndicesToTroops.entrySet()) {
            String[] split = continent.getKey().split("-");
            int fromCountryIndex = Integer.parseInt(split[0]);
            int toCountryIndex = Integer.parseInt(split[1]);
            // Iterate over all countries in one continent. If the player doesn't own one, continue to the next continent
            for (int i = fromCountryIndex; i <= toCountryIndex; i++) {
                if (!playerCountries.contains(gameBoard.getCountryFromID(i))) {
                    continue continentLoop;
                }
            }
            // If the player owns all countries of the continent, add the number of troops for owning that continent
            numTroops += continent.getValue();
        }
        return numTroops;
    }

    public List<Country> getConqueredCountries() {
        return conqueredCountries;
    }

    public void addConqueredCountry(Country conqueredCountry) {
        this.conqueredCountries.add(conqueredCountry);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public boolean isBot() {
        return players.getCurrentPlayer() instanceof RiskBot;
    }

    public Player getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    public ArrayList<Player>  getAllPlayer(){return players.getPlayers();}

    public boolean isCurrentPlayerBot() {
        return (players.getCurrentPlayer() instanceof RiskBot);
    }

    @Override
    public String toString() {
        return "Game{" +
                "\n\tplayers = " + players +
                "\n\tgamePhase = " + gamePhase +
                ",\n\tbattlePhase = " + battlePhase +
                "\n}";
    }

}
