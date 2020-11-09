package environment;

import gameelements.game.Game;
import gameelements.phases.GamePhase;
import gameelements.phases.data.DistributionEventData;
import gameelements.player.PlayerType;

import java.util.HashMap;

public class GameEnvironment {

    /**
     * The back-end of the game
     */
    private Game game;

    /**
     * The number of players in the game
     */
    private int numPlayers;

    /**
     * The type of the bot in the game
     */
    private PlayerType playerTypes;

    /**
     * Constructor
     * @param numPlayers The number of players
     * @param type The type of the players
     */
    public GameEnvironment(int numPlayers, PlayerType type) {
        assert(2 <= numPlayers && numPlayers <= 6);
        assert(type != null && type != PlayerType.USER);
        this.numPlayers = numPlayers; this.playerTypes = type;
        createGame();
    }

    /**
     * Constructor - 2 player game
     * @param type The type of the players
     */
    public GameEnvironment(PlayerType type) {
        this(2, type);
    }

    public void finishDistributionPhase() {
        while (game.getGamePhase().equals(GamePhase.DISTRIBUTION)) {
            game.onGameEvent(new DistributionEventData(-1));
        }
        System.out.println(game.getGameBoard());
    }

    public void train() {
        
    }

    /**
     * Creates the back-end of the game setting
     */
    private void createGame() {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        for (int i = 0; i < this.numPlayers; i++) {
            players.put(i, this.playerTypes);
        }
        this.game = new Game(players);
    }

    /**
     * Resets the game (e.g. creates a new instance of it)
     */
    public void reset() {
        createGame();
    }

}
