package environment;

import gameelements.game.Game;
import gameelements.phases.BattlePhase;
import gameelements.phases.GamePhase;
import gameelements.phases.data.AttackEventData;
import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.FortifyEventData;
import gameelements.phases.data.PlacementEventData;
import gameelements.player.DQNNBot;
import gameelements.player.LinearTDBot;
import gameelements.player.Player;
import gameelements.player.PlayerType;

import java.util.HashMap;
import java.util.List;

import static gameelements.player.PlayerType.TD;

public class GameEnvironment {

    /**
     * The back-end of the game
     */
    protected Game game;

    /**
     * The number of players in the game
     */
    private final int numPlayers;

    /**
     * The type of the bot in the game
     */
    private final PlayerType playerTypes;

    /**
     * Whether to enable file writing of statistics or not
     */
    private final boolean fileWriting;

    /**
     * Whether TD-Bot saves metrics per round or per game
     */
    private boolean perGame = false;

    /**
     * Whether TD-Bot saves metrics per round or per round
     */
    private boolean perRound = false;


    /**
     * Helper variable to save turns until player won
     */
    private int turnsUntilWin;

    /**
     * Constructor
     * @param numPlayers The number of players
     * @param type The type of the players
     */
    public GameEnvironment(int numPlayers, PlayerType type, boolean fileWriting) {
        assert(2 <= numPlayers && numPlayers <= 6);
        assert(type != null && type != PlayerType.USER);
        this.numPlayers = numPlayers; this.playerTypes = type;
        this.fileWriting = fileWriting;
        createGame();
    }

    public GameEnvironment(int numPlayers, PlayerType type) {
        this(numPlayers, type, false);
    }

    /**
     * Constructor - 2 player game
     * @param type The type of the players
     */
    public GameEnvironment(PlayerType type) {
        this(2, type);
    }

    public GameEnvironment(PlayerType type, boolean fileWriting) {
        this(2, type, fileWriting);
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
    public void trainOnOneGame(int maxTurns, boolean verbose) {
        int turnCounter = 0;
        double rewardSum = 0;
        turnsUntilWin = 0; //reset the counter
        while (turnCounter < maxTurns*numPlayers && !game.getGamePhase().equals(GamePhase.VICTORY)) {
            //System.out.println("-- Current Player: " + game.getCurrentPlayer() + " --");
            while (game.getBattlePhase().equals(BattlePhase.ATTACK) && !game.getGamePhase().equals(GamePhase.VICTORY)) {
                game.onGameEvent(new AttackEventData(-1, -1));
            }

            if (!game.getGamePhase().equals(GamePhase.VICTORY)) {
                finishFortifyingPhase();

                // Next player placement
                finishPlacementPhase();

            }
            System.out.println("##### Turn number "+turnCounter+" #####");
            turnCounter++;
            if(playerTypes == TD && perRound){
                int i = 0;
                List<Player> players = game.getAllPlayer();
                for(Player player : players) {
                    ((LinearTDBot) player).enableTurnMetrics();
                    ((LinearTDBot) player).prepWeights();
                    ((LinearTDBot) player).metrics.addToMetric("alpha", ((LinearTDBot) player).getAlpha());
                    ((LinearTDBot) player).metrics.addToMetric("lambda", ((LinearTDBot) player).getLambda());
                    ((LinearTDBot) player).metrics.addToMetric("winChanceThreshold", ((LinearTDBot) player).getWinChanceThreshold());
                    ((LinearTDBot) player).metrics.addToMetric("randomChanceThreshold", ((LinearTDBot) player).getrandomChanceThreshold());
                    ((LinearTDBot) player).metrics.addToMetric("armiesFeatureWeight", ((LinearTDBot) player).getArmiesFeatureWeight());
                    ((LinearTDBot) player).metrics.addToMetric("territoryFeatureWeight", ((LinearTDBot) player).getTerritoryFeatureWeight());
                    ((LinearTDBot) player).metrics.addToMetric("enemyReinforcementFeatureWeight", ((LinearTDBot) player).getEnemyReinforcementFeatureWeight());
                    ((LinearTDBot) player).metrics.addToMetric("bestEnemyFeatureWeight", ((LinearTDBot) player).getBestEnemyFeatureWeight());
                    ((LinearTDBot) player).metrics.addToMetric("hinterlandFeatureWeight", ((LinearTDBot) player).getHinterlandFeatureWeight());
                    ((LinearTDBot) player).metrics.addToMetric("stateValue", ((LinearTDBot) player).getCurrentStateValue());
                    System.out.println("Player ID: " + player.getId());
                    System.out.println(((LinearTDBot) player).getLambda());
                    ((LinearTDBot) player).metrics.saveToFile("src\\main\\java\\p" + i++ + ".txt");
                }

            }
        }

        //this is the case that the game did not end within the allowed amount of turns
        if(game.getGamePhase() == GamePhase.BATTLE){
            turnsUntilWin = -1;

        }

        if(game.getGamePhase().equals(GamePhase.VICTORY)) {
            List<Player> players = game.getAllPlayer();
            for(Player player : players){
                if(player.getId() != 1 && player.getCountriesOwned().size() > 0){ //whenever it is the bot that's being itterated and it wins i.e. owns all the countries
                    turnsUntilWin = turnCounter - 1; //we use the turn counter to set the turns until win
//                    System.out.println("We Have a victory in " + turnsUntilWin + " turns");
//                    System.out.println(game.getAllPlayer());
//                    System.out.println("player: " + player);
//                    System.out.println("Number of Countries they own: " + player.getCountriesOwned().size());
                }

                reset();
            }


        }
    }

    public void train(int numGames, int maxTurns, boolean verbose) {
        int gameNum = 0;
        while (gameNum < numGames) {
            long startTime = System.currentTimeMillis();
            finishDistributionPhase();
            finishPlacementPhase();
            trainOnOneGame(maxTurns, false);

            gameNum++;
            if (verbose) {
                System.out.println("Game " + gameNum + " - Phase: " + game.getGamePhase());
                System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime)/1. + " ms");
            }
            if(playerTypes == TD){ saveTDWeight(); }

            if(playerTypes == TD && perGame) {
                saveTDWeight();
                int i = 0;
                for (Player p : game.getAllPlayer()) {
                    ((LinearTDBot) p).enableGameMetrics();
                    ((LinearTDBot) p).metrics.addToMetric("alpha",((LinearTDBot) p).getAlpha());
                    ((LinearTDBot) p).metrics.addToMetric("lambda", ((LinearTDBot) p).getLambda());
                    ((LinearTDBot) p).metrics.addToMetric("winChanceThreshold", ((LinearTDBot) p).getWinChanceThreshold() );
                    ((LinearTDBot) p).metrics.addToMetric("randomChanceThreshold", ((LinearTDBot) p).getrandomChanceThreshold());
                    ((LinearTDBot) p).metrics.addToMetric("stateValue",((LinearTDBot) p).getCurrentStateValue());
                    ((LinearTDBot) p).metrics.addToMetric("turnsUntilWin",turnsUntilWin);
                    System.out.println("Player ID: " + p.getId());
                    System.out.println(((LinearTDBot) p).getLambda());
                    if (true) {
                        ((LinearTDBot) p).metrics.saveToFile("src\\main\\java\\p"+i+++".txt");
                    }
                }

            }
        }

        for (int i = 0; i < game.gamePlayers.size(); i++) {
            Player p = game.gamePlayers.get(i);
            if (p instanceof DQNNBot) ((DQNNBot) p).saveMetrics("D:\\Projects\\Project-2.1---Game\\src\\main\\java\\gameelements\\player\\player " + i);
        }

    }
    public void saveTDWeight(){
        System.out.println("###### saving weights ######");
        List<Player> players = game.getAllPlayer();
        Player best = players.get(0);
        int bestTerritory = -1;
        for (Player p: players) {
            int countriesOwned = p.getNumCountriesOwned();
            if (countriesOwned > bestTerritory) {
                best = p;
                bestTerritory = countriesOwned;
            }
        }
        String path = "src/main/java/gameelements/player/botWeights/TD-Bot_Weights.txt";
        ((LinearTDBot) best)
                .getLinearEvalFunction()
                .save(path);


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
        game.reset();
    }



    public void setPerGame(boolean switcher){
        perGame = switcher;
        System.out.println("Printing per game: " + switcher);
    }

    public void setPerRound(boolean switcher){
        perRound = switcher;
        System.out.println("Printing per turn " + switcher);
    }

}
