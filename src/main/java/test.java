import gameelements.game.Game;
import gameelements.player.PlayerType;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.USER);
        players.put(1, PlayerType.USER);
        Game game = new Game(players);

        System.out.println(game.getGamePhase());
        System.out.println(game.getBattlePhase());

    }
}
