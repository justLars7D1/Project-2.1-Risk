package gameelements.game;

import gameelements.data.BattleEventData;
import gameelements.data.DistributionEventData;
import gameelements.data.GameEventData;

/**
* Shared interface for accessing the players via the UI and the bot 
*/
public abstract class GameObserver {

    protected GamePhase gamePhase;

    protected GamePhase.BATTLE battlePhase;

    public GameObserver(GamePhase gamePhase, GamePhase.BATTLE battlePhase) {
        this.gamePhase = gamePhase;
        this.battlePhase = battlePhase;
    }

    public void onGameEvent(GameEventData data) {
        switch (gamePhase) {
            case DISTRIBUTION:
                onDistributionEvent((DistributionEventData) data);
                break;
            case VICTORY:
                onBattleEvent((BattleEventData) data);
                break;
        }
    }

    protected abstract void onDistributionEvent(DistributionEventData data);

    protected abstract void onBattleEvent(BattleEventData data);

}
