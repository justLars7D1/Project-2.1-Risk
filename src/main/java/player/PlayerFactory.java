package player;

public class PlayerFactory {

    /**
     * create players and equip them
     */
    private PlayerFactory(){}

    /**
     * configure player settings
     */
    public static Player createHumanPlayer(){
        return new RiskUser();
    }

    /**
     * set strategies and algorithms of our bot
     */
    public static Player createAIPlayer(){
        return new RiskBot();
    }
}
