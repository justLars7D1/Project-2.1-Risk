package gameelements.player;

public class PlayerFactory {

    /**
     * create players and equip them
     */
    private PlayerFactory(){}


    /**
     * configure gameelements.player settings
     */
    public static Player createHumanPlayer(int id, int numTroopsInInventory) {
        return new RiskUser(id, numTroopsInInventory);
    }

    /**
     * set strategies and algorithms of our bot
     */
    public static Player createAIPlayer(int id, int numTroopsInInventory) {
        return new RiskBot(id, numTroopsInInventory);
    }
}
