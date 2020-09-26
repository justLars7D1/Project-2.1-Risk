package gameelements.game;

import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.phases.data.DistributionEventData;
import org.junit.Test;
import settings.Settings;

import java.util.HashMap;

public class DistributionPhaseTest {

    @Test
    public void testDistributionPhaseUntilCountriesHaveOwner() {
        Game game = setupGame();
        for (int i = 0; i < Settings.countries.length; i++) {
            DistributionEventData data = new DistributionEventData(i);
            game.onGameEvent(data);
        }
        Board gameBoard = game.getGameBoard();
        for (int i = 0; i < Settings.countries.length; i++) {
            Country c = gameBoard.getCountryFromID(i);
            System.out.println(c);
        }
    }

    @Test
    public void testDistributionPhaseUntilNoMoreTroops() {
        Game game = setupGame();
        for (int i = 0; i < 80; i++) {
            DistributionEventData data = new DistributionEventData(i % Settings.countries.length);
            game.onGameEvent(data);
        }
        Board gameBoard = game.getGameBoard();
        for (int i = 0; i < Settings.countries.length; i++) {
            Country c = gameBoard.getCountryFromID(i);
            System.out.println(c);
        }
        System.out.println(game.getGamePhase());
    }

    private Game setupGame() {
        HashMap<Integer, Integer> players = new HashMap<>();
        players.put(0, 1);
        players.put(1, 1);
        return new Game(players);
    }

}
