package gameelements.player;

import gameelements.game.Game;

public class PlayerFactory {

    /**
     * configure gameelements.player settings
     */
    public static Player createPlayer(int id, int numTroopsInInventory, Game game, PlayerType type) {
        switch (type) {
            case USER:
                return new RiskUser(id, numTroopsInInventory);
            case TD:
                return new LinearTDBot(id, numTroopsInInventory, game);
            default:
                return new DQNNBot(id, numTroopsInInventory, game);
        }
    }
    
}
