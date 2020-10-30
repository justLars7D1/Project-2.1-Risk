package environment;

import gameelements.player.PlayerType;
import gameelements.game.Game;

public class GameEnvironment {

    Game game;

    public GameEnvironment(int numPlayers, PlayerType type) {
        assert(2 <= numPlayers && numPlayers <= 6 && type != null);


    }

}
