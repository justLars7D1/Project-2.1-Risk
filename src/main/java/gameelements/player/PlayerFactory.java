package gameelements.player;

public class PlayerFactory {

    /**
     * configure gameelements.player settings
     */
    public static Player createPlayer(int id, int numTroopsInInventory, PlayerType type) {
        switch (type) {
            case USER:
                return new RiskUser(id, numTroopsInInventory);
            case TD:
                return new LinearTDBot(id, numTroopsInInventory);
            default:
                return new DQNNBot(id, numTroopsInInventory);
        }
    }
    
}
