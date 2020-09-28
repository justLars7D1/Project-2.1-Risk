package gameelements.game;

import gameelements.phases.*;
import gameelements.phases.data.*;

/**
* Shared interface for accessing the game state via the UI and the bot
*/
public abstract class GameObserver {

    /**
     * The current phase of the game
     */
    protected GamePhase gamePhase;

    /**
     * The current stage of the battle phase
     */
    protected BattlePhase battlePhase;

    public GameObserver(GamePhase gamePhase, BattlePhase battlePhase) {
        this.gamePhase = gamePhase;
        this.battlePhase = battlePhase;
    }

    /**
     * Creates a game event given the data about the event
     * @param data The data about the event
     */
    public void onGameEvent(GameEventData data) {
        switch (gamePhase) {
            case DISTRIBUTION:
                onDistributionEvent((DistributionEventData) data);
                break;
            case BATTLE:
                onBattleEvent((BattleEventData) data);
                break;
            case VICTORY:
                break;
        }
    }

    /**
     * Handles the game action on the battle event
     * @param data The data about the event
     */
    private void onBattleEvent(BattleEventData data) {
        switch (battlePhase) {
            case PLACEMENT:
                onPlacementEvent((PlacementEventData) data);
                break;
            case ATTACK:
                onAttackEvent((AttackEventData) data);
                break;
            case FORTIFYING:
                onFortifyEvent((FortifyEventData) data);
                break;
        }
    }

    /**
     * Handles the game action on the distribution event
     * @param data The data about the event
     */
    protected abstract void onDistributionEvent(DistributionEventData data);

    /**
     * Handles the game action on the placement event
     * @param data The data about the event
     */
    protected abstract void onPlacementEvent(PlacementEventData data);

    /**
     * Handles the game action on the attack event
     * @param data The data about the event
     */
    protected abstract void onAttackEvent(AttackEventData data);

    /**
     * Handles the game action on the fortify event
     * @param data The data about the event
     */
    protected abstract void onFortifyEvent(FortifyEventData data);

    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    public void setBattlePhase(BattlePhase battlePhase) {
        this.battlePhase = battlePhase;
    }

    public BattlePhase getBattlePhase() {
        return this.battlePhase;
    }

}
