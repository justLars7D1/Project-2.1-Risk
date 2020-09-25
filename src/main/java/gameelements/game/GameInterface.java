package gameelements.game;

/**
 * Represents the main access point to the back-end of the game
 * TODO: Add methods for scoring, end of game, environment, etc...
 */
public interface GameInterface {

    /**
     * Starts the initial phase of the game where troops are placed
     */
    public void startPlacementPhase();

    /**
     * Starts the phase of the game in which players battle
     */
    public void startBattlePhase();

}
