package player;

public class PlayerFactory {

    /**
     * create players and equip them
     */
    private PlayerFactory(){}


    /**
     * configure player settings
     */
    public static Player createHumanPlayer(int id) {
        return new RiskUser(id);
    }

    /**
     * set strategies and algorithms of our bot
     */
    public static Player createAIPlayer(int id) {
        return new RiskBot(id);
    }
}
