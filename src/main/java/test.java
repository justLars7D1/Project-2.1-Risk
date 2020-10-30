import gameelements.game.Game;
import gameelements.player.PlayerType;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.PLAYER);
        players.put(1, PlayerType.PLAYER);
        Game game = new Game(players);

        System.out.println(game.getGamePhase());
        System.out.println(game.getBattlePhase());

    }
}
