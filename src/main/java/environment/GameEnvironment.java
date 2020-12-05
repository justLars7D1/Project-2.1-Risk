package environment;

import gameelements.game.Game;
import gameelements.phases.BattlePhase;
import gameelements.phases.GamePhase;
import gameelements.phases.data.AttackEventData;
import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.FortifyEventData;
import gameelements.phases.data.PlacementEventData;
import gameelements.player.DQNNBot;
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
        //System.out.println(game.getGameBoard());
    }

    public void finishPlacementPhase() {
        while (game.getBattlePhase().equals(BattlePhase.PLACEMENT)) {
            game.onGameEvent(new PlacementEventData(-1, 0));
        }
        //System.out.println(game.getGameBoard());
    }

    public void finishFortifyingPhase() {
        while (game.getBattlePhase().equals(BattlePhase.FORTIFYING)) {
            game.onGameEvent(new FortifyEventData(-1, -1, 0));
        }
    }

    /**
     * Approach: Freeze one network and train the other one
     * @param maxTurns The maximum number of turns before quitting the training
     */
    public void train(int maxTurns, boolean verbose) {
        int turnCounter = 0;
        while (turnCounter < maxTurns*numPlayers && !game.getGamePhase().equals(GamePhase.VICTORY)) {
            System.out.println("-- Current Player: " + game.getCurrentPlayer() + " --");
            while (game.getBattlePhase().equals(BattlePhase.ATTACK) && !game.getGamePhase().equals(GamePhase.VICTORY)) {
                game.onGameEvent(new AttackEventData(-1, -1));
            }

            if (!game.getGamePhase().equals(GamePhase.VICTORY)) {
                finishFortifyingPhase();

                // Next player placement
                finishPlacementPhase();

            }

            if (verbose) {
                System.out.println("Turn " + turnCounter + " completed");
            }
            turnCounter++;
        }

        System.out.println(game.getGameBoard());

        System.out.println("Phase: " + game.getGamePhase());

        System.out.println("Last network of current player:\n");
        System.out.println(((DQNNBot) game.getCurrentPlayer()).getEstimatorNetwork());

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
