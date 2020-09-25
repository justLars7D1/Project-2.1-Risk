import gameelements.game.Game;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, Integer> players = new HashMap<>();
        players.put(0, 1);
        players.put(1, 1);
        Game game = new Game(players);
        System.out.println(game);

    }
}
